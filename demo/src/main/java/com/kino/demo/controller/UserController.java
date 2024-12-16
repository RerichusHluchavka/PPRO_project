package com.kino.demo.controller;

import com.kino.demo.model.User;
import com.kino.demo.repository.UserRepository;
import com.kino.demo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {

    private UserService userService;
    private UserRepository userRepository;

    @Autowired
    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    public String registerUser(@Valid User user, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "register";
        }
        if(userRepository.findByUsername(user.getUsername()) != null) {
            return "register";
        }
        if (user.getUsername() != null || !user.getUsername().isEmpty()) {
            user.setRole("ADMIN");
        }
        String encryptedPassword = new BCryptPasswordEncoder().encode(user.getPassword());
        user.setPassword(encryptedPassword);
        userService.save(user);
        System.out.println("User registered:");
        return "redirect:/";
    }

}
