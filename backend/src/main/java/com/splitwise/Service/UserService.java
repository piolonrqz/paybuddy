package com.splitwise.Service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.splitwise.Repository.UserRepository;
import com.splitwise.Entities.User;
import java.util.Optional;


@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    //Register new user
    public User registerUser(User user){
        if(userRepository.existsByEmail(user.getEmail())){
            throw new RuntimeException("Email already exist");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    //Authenticate user
    public User loginUser(String email, String rawPassword){
        Optional<User> userOpt = userRepository.findByEmail(email);

        if(userOpt.isPresent()){
            User user = userOpt.get();

            if(passwordEncoder.matches(rawPassword, user.getPassword())){
                return user;
            }else{
                throw new RuntimeException("Invalid Password");
            }
        }else{
            throw new RuntimeException("User not found");
        }
    }

    //get user id
    public User getUserId(Long id) {
        return userRepository.findById(id)
                            .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
