package com.splitwise.Controller;

import org.springframework.web.bind.annotation.*;

import com.splitwise.Service.UserService;
import com.splitwise.Entities.User;



@RestController
@RequestMapping("/api/users")
public class UserController {
    
    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    //register user
    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
        return userService.registerUser(user);
    }
    
    //login user
    @PostMapping("/login")
    public User loginUser(@RequestParam String email, @RequestParam String rawPassword) {
        return userService.loginUser(email, rawPassword);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserId(id);
    }
    
}
