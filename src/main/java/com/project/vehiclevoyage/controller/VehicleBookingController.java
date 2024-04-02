package com.project.vehiclevoyage.controller;

import com.project.vehiclevoyage.entities.BookingDetails;
import com.project.vehiclevoyage.entities.User;
import com.project.vehiclevoyage.entities.Vehicle;
import com.project.vehiclevoyage.helper.Message;
import com.project.vehiclevoyage.service.BookingDetailsService;
import com.project.vehiclevoyage.service.UserDetailsServiceImplementation;
import com.project.vehiclevoyage.service.VehicleService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

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
    public String searchAvailableVehicles(@ModelAttribute BookingDetails bookingDetails, Model model, Principal principal, HttpSession session) {
            User user = (User) userDetailsService.loadUserByUsername(principal.getName());
        try {
            LocalDate endDate = calculateEndDate(bookingDetails); // Calculate end date based on booking type
            bookingDetails.setEndDate(endDate);

            // Fetch all vehicles of the selected type and city
            List<Vehicle> allVehicles = vehicleService.findByCityAndVehicleType(bookingDetails.getCity(), bookingDetails.getVehicleType());
            //Print id of all vehicles
            allVehicles.forEach(vehicle -> System.out.println(vehicle.getId()));

            // Fetch booked vehicles within the selected date range
            List<BookingDetails> bookedVehicles = bookingDetailsService.findBookedVehicles(
                    bookingDetails.getCity(), bookingDetails.getVehicleType(), bookingDetails.getStartDate(), bookingDetails.getEndDate());
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

            session.setAttribute("bookingDetails", bookingDetails);

            model.addAttribute("bookingDetails", bookingDetails);
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
    public String registeredVehicles(@PathVariable String registrationNumber,Model model, Principal principal, HttpSession session) {

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

    //Controller to open confirm booking details page
    @GetMapping("/user/confirm-booking/{registrationNumber}")
    @PreAuthorize("hasRole('USER')")
    public String getConfirmBookingPage(@PathVariable String registrationNumber, Model model, Principal principal, HttpSession session) {

        User user = (User) userDetailsService.loadUserByUsername(principal.getName());
        Vehicle vehicle = vehicleService.getVehicleByRegistrationNumber(registrationNumber);

        BookingDetails bookingDetails = (BookingDetails) session.getAttribute("bookingDetails");
        bookingDetails.setVehicleId(vehicle.getId());
        String pickupLocation = vehicle.getHouseNumber_shopNumber()+" "+vehicle.getColony()+" "+vehicle.getCity()+" "+vehicle.getState()+" "+vehicle.getCountry()+" "+vehicle.getPincode();
        bookingDetails.setPickupLocation(pickupLocation);
        System.out.println("Booking Details: " + bookingDetails.toString());

        //Calculate Total Cost
        double totalCost = 0;
        if ("Daily".equals(bookingDetails.getBookingType()) && bookingDetails.getVehicleType().equals("Car")) {
            totalCost = 1000 * Long.parseLong(bookingDetails.getDays());
        }
        else if ("Weekly".equals(bookingDetails.getBookingType()) && bookingDetails.getVehicleType().equals("Car")) {
            totalCost = (1000 * (Long.parseLong(bookingDetails.getWeeks()) * 7))*0.9;
        }
        else if ("Monthly".equals(bookingDetails.getBookingType()) && bookingDetails.getVehicleType().equals("Car")) {
            totalCost = (1000 * (Long.parseLong(bookingDetails.getMonths()) * 30))*0.8;
        }
        else if ("Daily".equals(bookingDetails.getBookingType()) && bookingDetails.getVehicleType().equals("Bike")) {
            totalCost = 500 * Long.parseLong(bookingDetails.getDays());
        }
        else if ("Weekly".equals(bookingDetails.getBookingType()) && bookingDetails.getVehicleType().equals("Bike")) {
            totalCost = (500 * (Long.parseLong(bookingDetails.getWeeks()) * 7))*0.9;
        }
        else if ("Monthly".equals(bookingDetails.getBookingType()) && bookingDetails.getVehicleType().equals("Bike")) {
            totalCost = (500 * (Long.parseLong(bookingDetails.getMonths()) * 30))*0.8;
        }

        bookingDetails.setTotalCost(totalCost);

        session.setAttribute("bookingDetails", bookingDetails);

        model.addAttribute("bookingDetails", bookingDetails);
        model.addAttribute("vehicle", vehicle);
        model.addAttribute("user", user);
        return "booking_confirmation_page";

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
