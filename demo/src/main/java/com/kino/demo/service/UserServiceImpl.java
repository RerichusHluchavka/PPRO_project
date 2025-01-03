package com.kino.demo.service;

import com.kino.demo.model.Ticket;
import com.kino.demo.model.User;
import com.kino.demo.repository.UserRepository;
import com.kino.demo.security.MyUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new MyUserDetails(user);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public User getUserById(long id){
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }


    @Override
    public User deleteUserByID(long id){
        Optional<User> userDB = userRepository.findById(id);
        if(userDB.isPresent()){
            User user = userDB.get();
            userRepository.delete(user);
            return user;
        }
        return null;
    }
    @Override
    public void updateUser(User user){
        Optional<User> userDB = userRepository.findById(user.getId());
        if(userDB.isPresent()){
            userRepository.save(user);
        }
    }
}
