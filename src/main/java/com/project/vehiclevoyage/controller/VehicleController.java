package com.project.vehiclevoyage.controller;

import com.project.vehiclevoyage.entities.User;
import com.project.vehiclevoyage.entities.Vehicle;
import com.project.vehiclevoyage.helper.Message;
import com.project.vehiclevoyage.repository.BrandRepoImpl;
import com.project.vehiclevoyage.repository.VehicleRepository;
import com.project.vehiclevoyage.service.UserDetailsServiceImplementation;
import com.project.vehiclevoyage.service.VehicleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.TimeZone;

@Controller
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private UserDetailsServiceImplementation userDetailsService;

    @Autowired
    private VehicleRepository vehicleRepository;

    //Extract vehicle details based on registration number
    //Example: localhost:8080/user/vehicle-details/ABCD1234
    @GetMapping("/user/vehicle-details/{registrationNumber}")
    @PreAuthorize("hasRole('USER')")
    public String registeredVehicles(@PathVariable String registrationNumber,Model model, Principal principal) {

            User user = (User) userDetailsService.loadUserByUsername(principal.getName());
            Vehicle vehicle = vehicleService.getVehicleByRegistrationNumber(registrationNumber);
            System.out.println("Vehicle: " + vehicle.toString());

            if(vehicle == null || !user.getId().equals(vehicle.getOwner().getId())){
                Vehicle nullvehicle = new Vehicle();
                model.addAttribute("message", new Message("Vehicle not found", "alert-danger"));
                model.addAttribute("title", "Vehicle Details-VehicleVoyage");
                model.addAttribute("vehicle", nullvehicle);
                model.addAttribute("user", user);
                return "vehicle_details";
            }

            System.out.println("Vehicle Details: " + vehicle.toString());
            model.addAttribute("title", "Vehicle Details-VehicleVoyage");
            model.addAttribute("user", user);
            model.addAttribute("vehicle", vehicle);
            return "vehicle_details";
    }
}
