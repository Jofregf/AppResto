package app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.services;

import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.security.JwtTokenProvider;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.dto.AdminUserDTO;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.dto.UserDTO;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.dto.UserResponse;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.entities.RoleModel;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.entities.UserModel;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.exceptions.ResourceNotFoundException;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.repositories.RoleRepository;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private RoleRepository roleRepository;


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
        user.setUserPassword(userDTO.getUserPassword());

        UserModel userUpdated = userRepository.save(user);

        return mapDTO(userUpdated);
    }

    @Override
    public void deleteUser(String token) {

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

        UserModel user = userRepository.findByUserEmailOrUserNameContainingIgnoreCase(usernameOrUserEmail, usernameOrUserEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with that username or email " + usernameOrUserEmail));

        String roleToken = jwtTokenProvider.getUserRoleFromToken(token);
        if(roleToken == null || !roleToken.equals("ROLE_ADMIN")){
            throw new AccessDeniedException("Access denied");
        }

        user.setEnabled(adminUserDTO.isEnabled());

        UserModel userUpdated = userRepository.save(user);

        return mapAdminDTO(userUpdated);
    }

    @Override
    public AdminUserDTO updateUserRole(AdminUserDTO adminUserDTO, String usernameOrUserEmail, String token) {

        UserModel user = userRepository.findByUserEmailOrUserNameContainingIgnoreCase(usernameOrUserEmail, usernameOrUserEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with that username or email " + usernameOrUserEmail));

        String roleToken = jwtTokenProvider.getUserRoleFromToken(token);
        if(roleToken == null || !roleToken.equals("ROLE_ADMIN")){
            throw new AccessDeniedException("Access denied");
        }

        if("admin".equals(adminUserDTO.getRole().getRoleName())){
            RoleModel role = roleRepository.findByRoleName("ROLE_ADMIN").get();
            user.setRole(role);
        } else if ("resto".equals(adminUserDTO.getRole().getRoleName())) {
            RoleModel role = roleRepository.findByRoleName("ROLE_RESTO").get();
            user.setRole(role);
        } else {
            RoleModel role = roleRepository.findByRoleName("ROLE_USER").get();
            user.setRole(role);
        }

        userRepository.save(user);

        return mapAdminDTO(user);

    }

    @Override
    public Optional<AdminUserDTO> findByUserNameOrEmail(String userNameOrEmail, String token) {

        String userRole = jwtTokenProvider.getUserRoleFromToken(token);
        if(userRole == null || !userRole.equals("ROLE_ADMIN")){
            throw new AccessDeniedException("Access denied");
        }

        Optional <UserModel> userOtional = userRepository
                .findByUserEmailOrUserNameContainingIgnoreCase(userNameOrEmail, userNameOrEmail);

        UserModel user = userOtional.orElseThrow(() -> new ResourceNotFoundException("User", "email or username", userNameOrEmail));

        return Optional.of(mapAdminDTO(user));
    }

    public UserDTO getUserById(String token) {

        String userId = jwtTokenProvider.getUserIdFromToken(token);
        UserModel user = userRepository
                .findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));

        return mapDTO(user);
    }
}
