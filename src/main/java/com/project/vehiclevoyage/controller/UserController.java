package com.project.vehiclevoyage.controller;

import com.project.vehiclevoyage.entities.User;
import com.project.vehiclevoyage.entities.Vehicle;
import com.project.vehiclevoyage.service.UserDetailsServiceImplementation;
import com.project.vehiclevoyage.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;

@Controller
public class UserController {
    @Autowired
    private UserDetailsServiceImplementation userDetailsService;

    @Autowired
    private VehicleService vehicleService;
    @ModelAttribute
    public void addUser(Model model, Principal principal) {
        User user = (User) userDetailsService.loadUserByUsername(principal.getName());
        model.addAttribute("user", user);
        System.out.println("User: " + user);
    }


    @GetMapping("/user/userdashboard")
    @PreAuthorize("hasRole('USER')")
    public String userdashboard(Model model, Principal principal) {
        User user = (User) userDetailsService.loadUserByUsername(principal.getName());
        model.addAttribute("vehicles", vehicleService.getVehiclesByUserId((user)));
        model.addAttribute("title", "User Dashboard-VehicleVoyage");
        return "userdashboard";
    }

}
