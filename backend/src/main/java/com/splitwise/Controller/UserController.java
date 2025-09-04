package com.splitwise.Controller;

import com.splitwise.DTO.UserDTO;
import com.splitwise.Entities.User;
import com.splitwise.Service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    //Register user
    @PostMapping("/register")
    public UserDTO register(@RequestBody User user) {
        return userService.registerUser(user);
    }

    //Login
    @PostMapping("/login")
    public UserDTO login(@RequestParam String email, @RequestParam String password) {
        return userService.loginUser(email, password);
    }

    //Get user by ID
    @GetMapping("/{id}")
    public UserDTO getUser(@PathVariable Long id) {
        return userService.getUserId(id);
    }
}
