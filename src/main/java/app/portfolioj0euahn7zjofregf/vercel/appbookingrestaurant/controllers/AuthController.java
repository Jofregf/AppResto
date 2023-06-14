package app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.controllers;

import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.dto.JwtAuthResponseDTO;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.security.JwtTokenProvider;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.dto.LoginDTO;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.dto.RegisterDTO;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.entities.RoleModel;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.entities.UserModel;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.repositories.RoleRepository;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.repositories.UserRepository;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.security.JwtTokenUtil;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.security.TokenRevocationStore;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
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

    @Autowired
    private TokenRevocationStore tokenRevocationStore;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponseDTO> authenticateUser(@RequestBody LoginDTO loginDTO) {
        Authentication authentication = authenticationManager
                .authenticate(
                        new UsernamePasswordAuthenticationToken(loginDTO.getUsernameOrEmail(), loginDTO.getPassword())
                );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenProvider.generateToken(authentication);
        String role = jwtTokenProvider.getUserRoleFromToken(token);
        String id = jwtTokenProvider.getUserIdFromToken(token);
        return ResponseEntity.ok(new JwtAuthResponseDTO(token, "Bearer", "successful login", role, id));
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

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {

        String token = jwtTokenUtil.getJwtFromRequest(request);
        SecurityContextHolder.clearContext();
        tokenRevocationStore.revokeToken(token);
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return ResponseEntity.ok("Logout exitoso");
    }
}
