package app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.services;

import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.entities.RoleModel;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.entities.UserModel;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.repositories.RoleRepository;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.repositories.UserRepository;
import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;

@Service
public class RoleServiceImpl {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    @Transactional
    public void init(){

        try{
            System.out.println("The application has started successfully, registering the roles.");

            if((roleRepository.findByRoleName("ROLE_ADMIN").isPresent())){

                if((userRepository.existsByRole_RoleName("ROLE_ADMIN"))){

                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "There is already a user with ADMIN role");

                } else {
                    UserModel newUser = new UserModel();
                    newUser.setUserName("admin");
                    newUser.setFirstName("admin");
                    newUser.setLastName("admin");
                    newUser.setUserPhone("26125011111");
                    newUser.setUserEmail("admin@correo.com");
                    newUser.setUserPassword(passwordEncoder.encode("123456"));
                    newUser.setEnabled(true);

                    RoleModel rol = roleRepository.findByRoleName("ROLE_ADMIN").get();
                    newUser.setRole(rol);
                    userRepository.save(newUser);
                }
         } else {
                RoleModel admin = new RoleModel();
                admin.setRoleName("ROLE_ADMIN");
                roleRepository.save(admin);
                RoleModel resto = new RoleModel();
                resto.setRoleName("ROLE_RESTO");
                roleRepository.save(resto);
                RoleModel user = new RoleModel();
                user.setRoleName("ROLE_USER");
                roleRepository.save(user);

                UserModel newUser = new UserModel();
                newUser.setUserName("admin");
                newUser.setFirstName("admin");
                newUser.setLastName("admin");
                newUser.setUserPhone("26125011111");
                newUser.setUserEmail("admin@correo.com");
                newUser.setUserPassword(passwordEncoder.encode("123456"));
                newUser.setEnabled(true);

                RoleModel rol = roleRepository.findByRoleName("ROLE_ADMIN").get();
                newUser.setRole(rol);
                userRepository.save(newUser);

            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

    }
}
