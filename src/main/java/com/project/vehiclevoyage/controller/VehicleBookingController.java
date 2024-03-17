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
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.List;

@Controller
public class VehicleBookingController {

    @Autowired
    private UserDetailsServiceImplementation userDetailsService;

    @Autowired
    private VehicleService vehicleService;

    //Controller to open vehicle booking page
    @GetMapping("/user/vehicle-booking")
    @PreAuthorize("hasRole('USER')")
    public String getVehicleBookingPage() {
        return "book-vehicle";
    }

    //Controller to open vehicle booking page and display available vehicles by city
    @GetMapping("user/vehicle-booking/{city}")
    @PreAuthorize("hasRole('USER')")
    public String getAvailableVehicles(@PathVariable String city, Model model, Principal principal) {
        User user = (User) userDetailsService.loadUserByUsername(principal.getName());
        List<Vehicle> vehicle = vehicleService.getVehicleByCity(city);

        model.addAttribute("user", user);
        model.addAttribute("vehicle", vehicle);

        return "book-vehicle";
    }
}
