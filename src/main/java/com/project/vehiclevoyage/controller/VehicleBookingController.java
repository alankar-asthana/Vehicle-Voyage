package com.project.vehiclevoyage.controller;

import com.project.vehiclevoyage.entities.BookingDetails;
import com.project.vehiclevoyage.entities.User;
import com.project.vehiclevoyage.entities.Vehicle;
import com.project.vehiclevoyage.helper.Message;
import com.project.vehiclevoyage.service.BookingDetailsService;
import com.project.vehiclevoyage.service.UserDetailsServiceImplementation;
import com.project.vehiclevoyage.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class VehicleBookingController {

    @Autowired
    private UserDetailsServiceImplementation userDetailsService;

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private BookingDetailsService bookingDetailsService;


    //Controller to open vehicle booking page
    @GetMapping("/user/vehicle-booking")
    @PreAuthorize("hasRole('USER')")
    public String getVehicleBookingPage(Model model, Principal principal) {

        User user = (User) userDetailsService.loadUserByUsername(principal.getName());
        model.addAttribute("user", user);
        return "book-vehicle";
    }

    //Controller to open vehicle booking page and display available vehicles by city
    @PostMapping("/user/available_vehicles")
    @PreAuthorize("hasRole('USER')")
    public String searchAvailableVehicles(@ModelAttribute BookingDetails bookingDetails, Model model, Principal principal) {
            User user = (User) userDetailsService.loadUserByUsername(principal.getName());
        try {
            LocalDate endDate = calculateEndDate(bookingDetails); // Calculate end date based on booking type

            // Fetch all vehicles of the selected type and city
            List<Vehicle> allVehicles = vehicleService.findByCityAndVehicleType(bookingDetails.getCity(), bookingDetails.getVehicleType());
            //Print id of all vehicles
            allVehicles.forEach(vehicle -> System.out.println(vehicle.getId()));

            // Fetch booked vehicles within the selected date range
            List<BookingDetails> bookedVehicles = bookingDetailsService.findBookedVehicles(
                    bookingDetails.getCity(), bookingDetails.getVehicleType(), bookingDetails.getStartDate(), endDate);
            //Print id of booked vehicles
            bookedVehicles.forEach(booking -> System.out.println(booking.getVehicleId()));

            // Filter out booked vehicles from all vehicles to get available vehicles
            List<Vehicle> availableVehicles = allVehicles.stream()
                    .filter(vehicle -> !bookedVehicles.stream().anyMatch(booking ->
                            booking.getVehicleId().equals(vehicle.getId())))
                    .collect(Collectors.toList());
            //Filter out vehicles which are owned by user itself
            availableVehicles = availableVehicles.stream()
                    .filter(vehicle -> !vehicle.getOwner().getId().equals(user.getId()))
                    .collect(Collectors.toList());

            if(availableVehicles.isEmpty()) {
                throw new Exception("No available vehicles found.");
            }

            model.addAttribute("vehicles", availableVehicles);
            model.addAttribute("user", user);
            return "book-vehicle"; // Return the name of the Thymeleaf template to display the search results
        } catch (Exception e) {
            model.addAttribute("message", new Message(e.getMessage(), "alert-danger"));
            model.addAttribute("user", user);
            return "book-vehicle";
        }
    }

    //Controller to display details of selected vehicle
    @GetMapping("/user/book/vehicle-details/{registrationNumber}")
    @PreAuthorize("hasRole('USER')")
    public String registeredVehicles(@PathVariable String registrationNumber,Model model, Principal principal) {

        User user = (User) userDetailsService.loadUserByUsername(principal.getName());
        Vehicle vehicle = vehicleService.getVehicleByRegistrationNumber(registrationNumber);
        System.out.println("Vehicle: " + vehicle.toString());

        if(vehicle == null){
            Vehicle nullvehicle = new Vehicle();
            model.addAttribute("message", new Message("Vehicle not found", "alert-danger"));
            model.addAttribute("title", "Vehicle Details-VehicleVoyage");
            model.addAttribute("vehicle", nullvehicle);
            model.addAttribute("user", user);
            return "booking_vehicle_details";
        }

        System.out.println("Vehicle Details: " + vehicle.toString());
        model.addAttribute("title", "Vehicle Details-VehicleVoyage");
        model.addAttribute("user", user);
        model.addAttribute("vehicle", vehicle);
        return "booking_vehicle_details";
    }

    //Method to calculate end date based on booking type
    private LocalDate calculateEndDate(BookingDetails bookingDetails) {
        LocalDate endDate = bookingDetails.getStartDate(); // Start with the selected start date

        if ("Daily".equals(bookingDetails.getBookingType())) {
            endDate = endDate.plusDays(Long.parseLong(bookingDetails.getDays())); // Add days for daily booking
        } else if ("Weekly".equals(bookingDetails.getBookingType())) {
            endDate = endDate.plusWeeks(Long.parseLong(bookingDetails.getWeeks())); // Add weeks for weekly booking
        } else if ("Monthly".equals(bookingDetails.getBookingType())) {
            endDate = endDate.plusMonths(Long.parseLong(bookingDetails.getMonths())); // Add months for monthly booking
        }
        return endDate;
    }
}
