package app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.controllers;

import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.dto.AdminUserDTO;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.dto.UserDTO;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.dto.UserResponse;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.services.DeleteBearerService;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private DeleteBearerService deleteBearerService;

    @PutMapping("/users")
    public ResponseEntity<UserDTO> updateUser(@Valid @RequestBody UserDTO userDTO,
                                              @RequestHeader(value="Authorization") String authorizHeader){

        String token = deleteBearerService.deleteBearerText(authorizHeader);
        UserDTO userResponse = userService.updateUser(userDTO, token);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    @DeleteMapping("/users")
    public ResponseEntity<String> deleteUser(@RequestHeader(value="Authorization") String authorizHeader){

        String token = deleteBearerService.deleteBearerText(authorizHeader);
        userService.deleteUser(token);
        return new ResponseEntity<>("User deleted succefull", HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/admin/users/{usernameOrUserEmail}")
    public ResponseEntity<AdminUserDTO> updateEnabled(@PathVariable String usernameOrUserEmail, @RequestBody AdminUserDTO userDTO,
                                                      @RequestHeader(value="Authorization") String authorizHeader){

        String token = deleteBearerService.deleteBearerText(authorizHeader);
        AdminUserDTO userResponse = userService.updateEnabled(userDTO, usernameOrUserEmail, token);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/users")
    public UserResponse userList(@RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
                                 @RequestParam(value = "pageSize", defaultValue = "9", required = false) int pageSize,
                                 @RequestHeader(value="Authorization") String authorizHeader){

        String token = deleteBearerService.deleteBearerText(authorizHeader);
        return userService.getUsers(pageNumber, pageSize, token);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/admin/users/{usernameOrUserEmail}/role")
    public ResponseEntity<AdminUserDTO> updateRol(@PathVariable String usernameOrUserEmail, @RequestBody AdminUserDTO userDTO,
                                                  @RequestHeader(value="Authorization") String authorizHeader){

        String token = deleteBearerService.deleteBearerText(authorizHeader);
        AdminUserDTO userResponse = userService.updateUserRole(userDTO, usernameOrUserEmail, token);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    @PreAuthorize(("hasRole('ROLE_ADMIN)"))
    @GetMapping("/admin/users/search/{userNameOrEmail}")
    public ResponseEntity<Optional<AdminUserDTO>> getUserByUserNameOrEmail(@PathVariable String userNameOrEmail,
                                                                      @RequestHeader(value="Authorization") String authorizHeader){

        String token = deleteBearerService.deleteBearerText(authorizHeader);
        return ResponseEntity.ok(userService.findByUserNameOrEmail(userNameOrEmail, token));
        }

    @GetMapping("/users")
    public ResponseEntity<UserDTO> getUserById(@RequestHeader(value="Authorization") String authorizHeader){

        String token = deleteBearerService.deleteBearerText(authorizHeader);
        System.out.println(token);
        return ResponseEntity.ok(userService.getUserById(token));
    }
}
