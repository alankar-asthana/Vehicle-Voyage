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

    //Update vehicle details
    @GetMapping("/user/updateVehicle/{registrationNumber}")
    @PreAuthorize("hasRole('USER')")
    public String getUpdateVehicleForm(@PathVariable String registrationNumber, Model model, Principal principal) {

        User user = (User) userDetailsService.loadUserByUsername(principal.getName());
        Vehicle vehicle = vehicleService.getVehicleByRegistrationNumber(registrationNumber);

        if(!user.getId().equals((vehicle.getOwner().getId()))) {
            model.addAttribute("title", "Vehicle Details-VehicleVoyage");
            model.addAttribute("user", user);
            model.addAttribute("vehicle", vehicle);
            model.addAttribute("message", new Message("You does not own this vehicle that's why you are not authorized to update this vehicle", "alert-danger"));
            return "update_vehicle_form";
        }

        if(vehicle == null) {
            Vehicle nullvehicle = new Vehicle();
            model.addAttribute("title", "Vehicle Details-VehicleVoyage");
            model.addAttribute("user", user);
            model.addAttribute("vehicle", nullvehicle);
            model.addAttribute("message", new Message("Vehicle not found", "alert-danger"));
            return "update_vehicle_form";
        }

        Date registrationDate = vehicle.getRegistrationDate();

        // Convert date to desired format
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC")); // Set timezone if needed
        String formattedDate = dateFormat.format(registrationDate);

        // Add formatted date to the model
        model.addAttribute("registrationDate", formattedDate);

        System.out.println("Vehicle Details: " + vehicle.toString());
        model.addAttribute("title", "Vehicle Details-VehicleVoyage");
        model.addAttribute("user", user);
        model.addAttribute("vehicle", vehicle);
        return "update_vehicle_form";
    }

   //Post request to update vehicle details
    @PostMapping("/user/update-vehicle-details")
    @PreAuthorize("hasRole('USER')")
    public String updateVehicleDetails(@ModelAttribute @Valid Vehicle updatedVehicle, BindingResult bindingResult,
                                       Model model, Principal principal) {

        try {

            User user = (User) userDetailsService.loadUserByUsername(principal.getName());

            if (bindingResult.hasErrors()) {
                throw new Exception(bindingResult.getFieldError().getDefaultMessage());
            }

            Vehicle savedVehicle = this.vehicleRepository.save(updatedVehicle);
            if(savedVehicle == null) {
                throw new Exception("Vehicle not found");
            }
            System.out.println("Updated Vehicle Details: " + savedVehicle.toString());

            model.addAttribute("title", "Vehicle Details-VehicleVoyage");
            model.addAttribute("user", user);
            model.addAttribute("vehicle", savedVehicle);
            model.addAttribute("message", new Message("Vehicle details updated successfully", "alert-success"));
            return "vehicle_details";

        }catch (Exception e) {
            User user = (User) userDetailsService.loadUserByUsername(principal.getName());

            model.addAttribute("title", "Vehicle Details-VehicleVoyage");
            model.addAttribute("user", user);
            model.addAttribute("vehicle", updatedVehicle);
            model.addAttribute("message", new Message(e.getMessage(), "alert-danger"));
            return "vehicle_details";
        }
    }

}
//model.addAttribute("vehicles", vehicleService.getVehiclesByUserId((user)));