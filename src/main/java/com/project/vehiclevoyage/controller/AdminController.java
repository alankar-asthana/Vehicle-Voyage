package com.project.vehiclevoyage.controller;

import com.google.api.services.gmail.Gmail;
import com.project.vehiclevoyage.entities.BookingDetails;
import com.project.vehiclevoyage.entities.ReviewRating;
import com.project.vehiclevoyage.entities.User;
import com.project.vehiclevoyage.entities.Vehicle;
import com.project.vehiclevoyage.service.BookingDetailsService;
import com.project.vehiclevoyage.service.ReviewRatingService;
import com.project.vehiclevoyage.service.UserService;
import com.project.vehiclevoyage.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;
import java.util.Comparator;
import java.util.List;

@Controller
public class AdminController {
    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    UserService userService;

    @Autowired
    VehicleService vehicleService;

    @Autowired
    BookingDetailsService bookingService;

    @Autowired
    ReviewRatingService reviewRatingService;

    //Adding admin details
    @ModelAttribute
    public void addUser(Model model, Principal principal) {
        User user = (User) userDetailsService.loadUserByUsername(principal.getName());
        model.addAttribute("user", user);
        System.out.println("User: " + user);
    }

    //Admin Dashboard
    @GetMapping("/admin/admindashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public String admindashboard(Model model, Principal principal) {
        model.addAttribute("title", "Admin Dashboard-VehicleVoyage");
        return "admindashboard";
    }

    //Fetch list of all users
    @GetMapping("/admin/user-profiles")
    @PreAuthorize("hasRole('ADMIN')")
    public String userProfiles(Model model, Principal principal) {
        // Fetch List of users
        List<User> users = userService.getUsers();

        model.addAttribute("users", users);
        model.addAttribute("title", "User Profile Management-VehicleVoyage");
        return "userprofiles";
    }

    //Fetch list of all vehicles
    @GetMapping("/admin/vehicle-list")
    @PreAuthorize("hasRole('ADMIN')")
    public String vehicleList(Model model, Principal principal) {
        //Fetch list of vehicles
        List<Vehicle> vehicles = vehicleService.getVehicles();

        //Sort the list by group of vehicles having same owner
        vehicles.sort(Comparator.comparing((Vehicle vehicle) -> vehicle.getOwner().getId())
                .thenComparing(Vehicle::getId));

        model.addAttribute("vehicles", vehicles);
        model.addAttribute("title", "Vehicle Management-VehicleVoyage");
        return "registeredvehicles";
    }

    //Fetch list of bookings
    @GetMapping("/admin/bookings")
    @PreAuthorize("hasRole('ADMIN')")
    public String bookings(Model model, Principal principal) {
        //Fetch list of bookings
        List<BookingDetails> Bookings = bookingService.getBookings();

        //Sort the list by group of bookings on the basis of booking status
        Bookings.sort(Comparator.comparing((BookingDetails booking) -> booking.getBookingStatus())
                .thenComparing(BookingDetails::getId));

        model.addAttribute("bookings", Bookings);
        model.addAttribute("title", "Booking Management-VehicleVoyage");
        return "bookingdetails";
    }
    // Fetch list of feedback
    @GetMapping("/admin/feedback")
    @PreAuthorize("hasRole('ADMIN')")
    public String feedback(Model model, Principal principal) {
        //Fetch list of feedback
        List<ReviewRating> feedbacks = reviewRatingService.getFeedbacks();
        //Sort the list by descending order of ratings
        feedbacks.sort(Comparator.comparing((ReviewRating feedback) -> feedback.getRating())
                .reversed());

        model.addAttribute("title", "Feedback Management-VehicleVoyage");
        model.addAttribute("feedbacks", feedbacks);
        return "feedbacks";
    }
}
