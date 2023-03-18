package app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.controllers;

import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.dto.UserDTO;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.dto.UserResponse;
import app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public UserResponse userList(@RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
                                 @RequestParam(value = "pageSize", defaultValue = "9", required = false) int pageSize){
        return userService.getUsers(pageNumber, pageSize);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable String userId){
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @PostMapping("/user")
    public ResponseEntity<UserDTO> saveUser(@RequestBody UserDTO userDTO){
        return new ResponseEntity<>(userService.createUser(userDTO), HttpStatus.CREATED);
    }

    @PutMapping("/user/{userId}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable String userId, @RequestBody UserDTO userDTO){
        UserDTO userResponse = userService.updateUser(userDTO, userId);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    @DeleteMapping("/user/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable String userId){
        userService.deleteUser(userId);
        return new ResponseEntity<>("User deleted succefull", HttpStatus.OK);
    }

    @PutMapping("/admin/user/{userId}")
    public ResponseEntity<UserDTO> updateEnabled(@PathVariable String userId, @RequestBody UserDTO userDTO){
        UserDTO userResponse = userService.updateEnabled(userDTO, userId);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }
}
