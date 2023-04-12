package app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.controllers;

import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.security.JwtAuthResponseDTO;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.security.JwtTokenProvider;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.dto.LoginDTO;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.dto.RegisterDTO;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.entities.RoleModel;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.entities.UserModel;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.repositories.RoleRepository;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.repositories.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponseDTO> authenticateUser(@RequestBody LoginDTO loginDTO) {
        Authentication authentication = authenticationManager
                .authenticate(
                        new UsernamePasswordAuthenticationToken(loginDTO.getUsernameOrEmail(), loginDTO.getPassword())
                );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenProvider.generateToken(authentication);

        return ResponseEntity.ok(new JwtAuthResponseDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity<?> userRegister(@Valid @RequestBody RegisterDTO registerDTO){
        if(userRepository.existsByUserName(registerDTO.getUserName())){
            return new ResponseEntity<>("The username already exists", HttpStatus.BAD_REQUEST);
        }
        if(userRepository.existsByUserEmail(registerDTO.getUserEmail())){
            return new ResponseEntity<>("The email already exists", HttpStatus.BAD_REQUEST);
        }
        UserModel newUser = new UserModel();
        newUser.setUserName(registerDTO.getUserName());
        newUser.setFirstName(registerDTO.getFirstName());
        newUser.setLastName(registerDTO.getLastName());
        newUser.setUserPhone(registerDTO.getUserPhone());
        newUser.setUserEmail(registerDTO.getUserEmail());
        newUser.setUserPassword(passwordEncoder.encode(registerDTO.getUserPassword()));
        newUser.setEnabled(true);

        RoleModel rol = roleRepository.findByRoleName("ROLE_USER").get();
        newUser.setRole(rol);

        userRepository.save(newUser);
        return new ResponseEntity<>("Registered user successfully", HttpStatus.OK);
    }

}
