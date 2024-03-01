package com.project.vehiclevoyage.controller;

import com.project.vehiclevoyage.entities.User;
import com.project.vehiclevoyage.service.UserDetailsServiceImplementation;
import com.project.vehiclevoyage.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserDetailsServiceImplementation userDetailsService;

    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("title", "Home-VehicleVoyage");
        return "home";
    }
    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("title", "Sign Up-VehicleVoyage");
        model.addAttribute("user", new User());
        return "signup";
    }
    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("title", "Login-VehicleVoyage");
        return "login";
    }

    @GetMapping("/uploadimage")
    public String uploadImage(Model model) {
        return "uploadimage";
    }
}
