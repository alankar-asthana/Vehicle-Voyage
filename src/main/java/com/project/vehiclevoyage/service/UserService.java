package com.project.vehiclevoyage.service;

import com.project.vehiclevoyage.entities.User;
import com.project.vehiclevoyage.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    //Method to fetch list of all users having role = USER
    public List<User> getUsers() {
        List<User> users = userRepository.findAll();
        //Remove users having role="ADMIN"
        users.removeIf(user -> user.getRole().equals("ADMIN"));
        return users;
    }

}
