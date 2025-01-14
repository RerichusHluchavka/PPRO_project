package com.kino.demo.controller;

import com.kino.demo.model.User;
import com.kino.demo.repository.UserRepository;
import com.kino.demo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    public String registerUser(@Valid User user, BindingResult bindingResult, Model model, Boolean role) {
        if(bindingResult.hasErrors()) {
            model.addAttribute("errorMessage", "Binding Result has errors");
            return "register";
        }
        if(userRepository.findByUsername(user.getUsername()) != null) {
            model.addAttribute("errorMessage", "User already exists");
            return "register";
        }
        if (role != null) {
            user.setRole("ADMIN");
        }
        String encryptedPassword = new BCryptPasswordEncoder().encode(user.getPassword());
        user.setPassword(encryptedPassword);
        userService.save(user);
        return "redirect:/login";
    }

}
