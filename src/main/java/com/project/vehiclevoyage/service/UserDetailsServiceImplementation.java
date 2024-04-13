package com.project.vehiclevoyage.service;

import com.project.vehiclevoyage.entities.User;
import com.project.vehiclevoyage.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImplementation implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByEmail(username);
        if (user == null) {
            throw new RuntimeException("User not found !!");
        }
        return (UserDetails) user;
    }

    public User registerUser(User user) {
        return userRepository.save(user);
    }
}
