package app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.services;

import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.dto.*;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.exceptions.RestoAppException;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.security.JwtTokenProvider;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.entities.RoleModel;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.entities.UserModel;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.exceptions.ResourceNotFoundException;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.repositories.RoleRepository;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.repositories.UserRepository;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.utilities.EmailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    // Entity to DTO
    private UserDTO mapDTO(UserModel userModel){

        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(userModel.getUserId());
        userDTO.setUserName(userModel.getUserName());
        userDTO.setFirstName(userModel.getFirstName());
        userDTO.setLastName(userModel.getLastName());
        userDTO.setUserPhone(userModel.getUserPhone());
        userDTO.setUserEmail(userModel.getUserEmail());
        userDTO.setUserPassword(userModel.getUserPassword());
        userDTO.setEnabled(userModel.getEnabled());

        return userDTO;
    }

    private AdminUserDTO mapAdminDTO(UserModel userModel){

        AdminUserDTO adminUserDTO = new AdminUserDTO();
        adminUserDTO.setUserName(userModel.getUserName());
        adminUserDTO.setFirstName(userModel.getFirstName());
        adminUserDTO.setLastName(userModel.getLastName());
        adminUserDTO.setUserPhone(userModel.getUserPhone());
        adminUserDTO.setUserEmail(userModel.getUserEmail());
        adminUserDTO.setEnabled(userModel.getEnabled());
        adminUserDTO.setRole(userModel.getRole());

        return adminUserDTO;
    }

    private UserPasswordDTO mapPass(UserModel userModel){

        UserPasswordDTO userPasswordDTO = new UserPasswordDTO();
        userPasswordDTO.setUserPassword(userModel.getUserPassword());

        return userPasswordDTO;
    }

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private EmailSender emailSender;

    @Autowired
    public UserServiceImpl(EmailSender emailSender) {
        this.emailSender = emailSender;
    }

    @Override
    public UserResponse getUsers(int pageNumber, int pageSize, String token) {

        String roleToken = jwtTokenProvider.getUserRoleFromToken(token);
        if(roleToken == null || !roleToken.equals("ROLE_ADMIN")){
            throw new AccessDeniedException("Access denied");
        }

        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Page<UserModel> users = userRepository.findAll(pageable);

        List<UserModel> usersList = users.getContent();

        List<AdminUserDTO> content = usersList.stream().map(user -> mapAdminDTO(user)).collect(Collectors.toList());

        UserResponse userResponse = new UserResponse();
        userResponse.setContents(content);
        userResponse.setPageNumber(users.getNumber());
        userResponse.setPageSize(users.getSize());
        userResponse.setTotalElements(users.getTotalElements());
        userResponse.setTotalPages(users.getTotalPages());
        userResponse.setLast(users.isLast());

        return userResponse;
    }

    @Override
    public UserDTO updateUser(UserDTO userDTO, String token) {

        if (!jwtTokenProvider.validateToken(token)) {
            throw new RestoAppException(HttpStatus.BAD_REQUEST, "Expired token");
        }

        String userId = jwtTokenProvider.getUserIdFromToken(token);
        UserModel user = userRepository
                .findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));

        String idToken = jwtTokenProvider.getUserIdFromToken(token);
        if(idToken == null || !idToken.equals((userId))){
            throw new AccessDeniedException("Access denied");
        }

        user.setUserName(userDTO.getUserName());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setUserPhone(userDTO.getUserPhone());
        user.setUserEmail(userDTO.getUserEmail());
        user.setUserPassword(user.getUserPassword());

        UserModel userUpdated = userRepository.save(user);

        return mapDTO(userUpdated);
    }

    @Override
    public void deleteUser(String token) {

        if (!jwtTokenProvider.validateToken(token)) {
            throw new RestoAppException(HttpStatus.BAD_REQUEST, "Expired token");
        }

        String userId = jwtTokenProvider.getUserIdFromToken(token);
        UserModel user = userRepository
                .findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));

        String idToken = jwtTokenProvider.getUserIdFromToken(token);
        if(idToken == null || !idToken.equals((userId))){
            throw new AccessDeniedException("Access denied");
        }

        userRepository.delete(user);
    }

    @Override
    public AdminUserDTO updateEnabled(AdminUserDTO adminUserDTO, String usernameOrUserEmail, String token) {

        if (!jwtTokenProvider.validateToken(token)) {
            throw new RestoAppException(HttpStatus.BAD_REQUEST, "Expired token");
        }

        UserModel user = userRepository.findByUserEmailOrUserNameContainingIgnoreCase(usernameOrUserEmail, usernameOrUserEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with that username or email " + usernameOrUserEmail));

        String roleToken = jwtTokenProvider.getUserRoleFromToken(token);
        if(roleToken == null || !roleToken.equals("ROLE_ADMIN")){
            throw new AccessDeniedException("Access denied");
        }

        user.setEnabled(adminUserDTO.isEnabled());

        UserModel userUpdated = userRepository.save(user);

        if(user.getEnabled() == false) {
            String message = "Estimado " + user.getUserName() + ":<br><br>"
                    + "Lamentamos informarle que su cuenta ha sido suspendida debido a un incumplimiento de las reglas " +
                    "de convivencia. Si considera que ha habido un error o desea que reevaluemos su situación, " +
                    "le invitamos a responder a este correo electrónico y proporcionar una explicación detallada de " +
                    "su situación.<br><br>"
                    + "Nos comprometemos a revisar cuidadosamente su caso y tomar las acciones apropiadas en base a la " +
                    "información que nos proporcione. Agradecemos su cooperación y entendimiento.<br><br>"
                    + "Saludos cordiales,<br>"
                    + "<span style=\"color: #F15422;\"> Equipo de Resto-Reservas </span><br><br>";
            emailSender.sendEmail(user.getUserEmail(), "Baneado", message);

        } else {
            String message = "Estimado " + user.getUserName() + ":<br><br>"
                    + "Luego de revisar su situación, hemos tomado la decisión de levantar la suspensión de su cuenta.<br><br>"
                    + "Agradecemos su cooperación y entendimiento durante este proceso. Si tiene alguna pregunta adicional " +
                    "o necesita más información, no dude en contactarnos.<br><br>"
                    + "Saludos cordiales,<br>"
                    + "<span style=\"color: #F15422;\"> Equipo de Resto-Reservas </span><br><br>";
            emailSender.sendEmail(user.getUserEmail(), "Desbaneado", message);
        }

        return mapAdminDTO(userUpdated);
    }

    @Override
    public AdminUserDTO updateUserRole(AdminUserDTO adminUserDTO, String usernameOrUserEmail, String token) {

        if (!jwtTokenProvider.validateToken(token)) {
            throw new RestoAppException(HttpStatus.BAD_REQUEST, "Expired token");
        }

        UserModel user = userRepository.findByUserEmailOrUserNameContainingIgnoreCase(usernameOrUserEmail, usernameOrUserEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with that username or email " + usernameOrUserEmail));

        String roleToken = jwtTokenProvider.getUserRoleFromToken(token);
        if(roleToken == null || !roleToken.equals("ROLE_ADMIN")){
            throw new AccessDeniedException("Access denied");
        }

        if("admin".equals(adminUserDTO.getRole().getRoleName())){
            RoleModel role = roleRepository.findByRoleName("ROLE_ADMIN").get();
            user.setRole(role);
            String text = "Estimado " + user.getUserName() + ":<br><br>" +
                    "Felicitaciones, ahora usted ha sido asignado como administrador en nuestra plataforma. " +
                    "Al ingresar a su cuenta, podrá acceder a las herramientas y funciones pertinentes para gestionar " +
                    "y administrar el sistema.<br><br>" +
                    "Si necesita ayuda adicional o tiene alguna pregunta, no dude en ponerse en contacto con nuestro " +
                    "equipo de soporte. Estaremos encantados de asistirle en todo lo que necesite.<br><br>" +
                    "Atentamente,<br>" +
                    "<span style=\"color: #F15422;\">Equipo de Resto-Reservas</span><br><br>";

            emailSender.sendEmail(user.getUserEmail(), "Rol Administrador", text);
        } else if ("resto".equals(adminUserDTO.getRole().getRoleName())) {
            RoleModel role = roleRepository.findByRoleName("ROLE_RESTO").get();
            user.setRole(role);
            String text = "Estimado " + user.getUserName() + ":<br><br>" +
                    "Felicitaciones, ahora usted ha sido asignado como representante de un restaurante en nuestra plataforma. " +
                    "Al ingresar a su cuenta, podrá acceder a las herramientas y funciones pertinentes para registrar, " +
                    "gestionar y administrar su restaurante.<br><br>" +
                    "Si necesita ayuda adicional o tiene alguna pregunta, no dude en ponerse en contacto con nuestro " +
                    "equipo de soporte. Estaremos encantados de asistirle en todo lo que necesite.<br><br>" +
                    "Atentamente,<br>" +
                    "<span style=\"color: #F15422;\">Equipo de Resto-Reservas</span><br><br>";

            emailSender.sendEmail(user.getUserEmail(), "Rol Restaurante", text);
        } else {
            RoleModel role = roleRepository.findByRoleName("ROLE_USER").get();
            user.setRole(role);

            String text = "Estimado " + user.getUserName() + ":<br><br>" +
                    "Gracias por ser parte de nuestra plataforma. Queremos informarle que su rol ha sido actualizado a usuario. " +
                    "Al ingresar a su cuenta, tendrá acceso a las herramientas y funciones básicas de la página para disfrutar de " +
                    "nuestros servicios.<br><br>" +
                    "Si necesita ayuda adicional o tiene alguna pregunta, no dude en ponerse en contacto con nuestro " +
                    "equipo de soporte. Estaremos encantados de asistirle en todo lo que necesite.<br><br>" +
                    "Atentamente,<br>" +
                    "<span style=\"color: #F15422;\">Equipo de Resto-Reservas</span><br><br>";

            emailSender.sendEmail(user.getUserEmail(), "Rol Usuario", text);
        }

        userRepository.save(user);

        return mapAdminDTO(user);

    }

    @Override
    public Optional<AdminUserDTO> findByUserNameOrEmail(String userNameOrEmail, String token) {

        if (!jwtTokenProvider.validateToken(token)) {
            throw new RestoAppException(HttpStatus.BAD_REQUEST, "Expired token");
        }

        String userRole = jwtTokenProvider.getUserRoleFromToken(token);
        if(userRole == null || !userRole.equals("ROLE_ADMIN")){
            throw new AccessDeniedException("Access denied");
        }

        Optional <UserModel> userOtional = userRepository
                .findByUserEmailOrUserNameContainingIgnoreCase(userNameOrEmail, userNameOrEmail);

        UserModel user = userOtional.orElseThrow(() -> new ResourceNotFoundException("User", "email or username", userNameOrEmail));

        return Optional.of(mapAdminDTO(user));
    }

    @Override
    public UserDTO getUserById(String token) {

        if (!jwtTokenProvider.validateToken(token)) {
            throw new RestoAppException(HttpStatus.BAD_REQUEST, "Expired token");
        }

        String userId = jwtTokenProvider.getUserIdFromToken(token);
        UserModel user = userRepository
                .findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));

        String idToken = jwtTokenProvider.getUserIdFromToken(token);
        if(idToken == null || !idToken.equals(userId)){
            throw new AccessDeniedException("Access denied");
        }

        return mapDTO(user);
    }

    @Override
    public UserPasswordDTO updatePassword(UserPasswordDTO userPasswordDTO, String token) {

        if (!jwtTokenProvider.validateToken(token)) {
            throw new RestoAppException(HttpStatus.BAD_REQUEST, "Expired token");
        }

        String userId = jwtTokenProvider.getUserIdFromToken(token);
        UserModel user = userRepository
                .findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));

        String idToken = jwtTokenProvider.getUserIdFromToken(token);
        if(idToken == null || !idToken.equals((userId))){
            throw new AccessDeniedException("Access denied");
        }
        user.setUserPassword(passwordEncoder.encode(userPasswordDTO.getUserPassword()));

        UserModel userUpdated = userRepository.save(user);

        return mapPass(userUpdated);
    }

    @Override
    public UserPasswordDTO forgotPassword(UserNamePassDTO userNamePassDTO) {

        String userNameOrEmail = userNamePassDTO.getUsernameOrEmail();
        Optional <UserModel> userOtional = userRepository
                .findByUserEmailOrUserNameContainingIgnoreCase(userNameOrEmail, userNameOrEmail);
        UserModel user = userOtional.orElseThrow(() -> new ResourceNotFoundException("User", "email or username", userNameOrEmail));

        final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        Random random = new Random();
        int length = random.nextInt(15 - 8 + 1) + 8;
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            sb.append(randomChar);
        }

        String password = sb.toString();
        String text = "Estimado " + user.getUserName() + ":<br><br>" +
                "Hemos generado una nueva contraseña para su cuenta. A continuación, encontrará los detalles de " +
                "inicio de sesión:<br><br>" +
                "<span style=\"color: #F15422; font-size: 25px;\">Contraseña: " + password + "</span><br><br>" +
                "Por favor, copie la contraseña y utilícela para iniciar sesión en su cuenta. Le recomendamos cambiarla " +
                "una vez haya accedido a su cuenta por motivos de seguridad.<br><br>" +
                "Si necesita ayuda adicional o tiene alguna pregunta, no dude en ponerse en contacto con nuestro equipo " +
                "de soporte.<br><br>" +
                "Atentamente,<br>" +
                "<span style=\"color: #F15422;\"> Equipo de Resto-Reservas </span><br><br>";

        emailSender.sendEmail(user.getUserEmail(), "Recuperación de contraseña", text);

        user.setUserPassword(passwordEncoder.encode(password));

        UserModel userUpdated = userRepository.save(user);

        return mapPass(userUpdated);
    }
}
