package app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.controllers;

import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.dto.AdminUserDTO;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.dto.UserDTO;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.dto.UserResponse;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users/{userId}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable String userId,
                                               @RequestHeader(value="Authorization") String authorizationHeader){
        String token = authorizationHeader.replace("Bearer ", "");

        return ResponseEntity.ok(userService.getUserById(userId, token));
    }

    @PutMapping("/users/{userId}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable String userId, @Valid @RequestBody UserDTO userDTO,
                                              @RequestHeader(value="Authorization") String authorizationHeader){
        String token = authorizationHeader.replace("Bearer ", "");
        UserDTO userResponse = userService.updateUser(userDTO, userId, token);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable String userId,
                                             @RequestHeader(value="Authorization") String authorizationHeader){
        String token = authorizationHeader.replace("Bearer ", "");
        userService.deleteUser(userId, token);
        return new ResponseEntity<>("User deleted succefull", HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/users/{userId}")
    public ResponseEntity<AdminUserDTO> updateEnabled(@PathVariable String userId, @RequestBody AdminUserDTO userDTO,
                                                      @RequestHeader(value="Authorization") String authorizationHeader){
        String token = authorizationHeader.replace("Bearer ", "");
        AdminUserDTO userResponse = userService.updateEnabled(userDTO, userId, token);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/username/{userName}")
    public ResponseEntity<AdminUserDTO> getUserByUserName(@PathVariable String userName,
                                                          @RequestHeader(value="Authorization") String authorizationHeader){
        String token = authorizationHeader.replace("Bearer ", "");
        return ResponseEntity.ok(userService.findByUserName(userName, token));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/admin/useremail/{userEmail}")
    public ResponseEntity<AdminUserDTO> getUserByUserEmail(@PathVariable String userEmail,
                                                           @RequestHeader(value="Authorization") String authorizationHeader){
        String token = authorizationHeader.replace("Bearer ", "");
        return ResponseEntity.ok(userService.findByUserEmail(userEmail, token));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/users")
    public UserResponse userList(@RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
                                 @RequestParam(value = "pageSize", defaultValue = "9", required = false) int pageSize,
                                 @RequestHeader(value="Authorization") String authorizationHeader){

        String token = authorizationHeader.replace("Bearer ", "");
        return userService.getUsers(pageNumber, pageSize, token);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/users/{userId}/role")
    public ResponseEntity<AdminUserDTO> updateRol(@PathVariable String userId, @RequestBody AdminUserDTO userDTO,
                                                  @RequestHeader(value="Authorization") String authorizationHeader){
        String token = authorizationHeader.replace("Bearer ", "");
        AdminUserDTO userResponse = userService.updateUserRole(userDTO, userId, token);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }
}
