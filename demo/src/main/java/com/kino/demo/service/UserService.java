package com.kino.demo.service;

import com.kino.demo.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService extends UserDetailsService {
    User findByUsername(String username);
    User getUserById(long id);
    User deleteUserByID(long userId);
    void save(User user);
    void updateUser(User user);
    List<User> getAllUsers();
}
