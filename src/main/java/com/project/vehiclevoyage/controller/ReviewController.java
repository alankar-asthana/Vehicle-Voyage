package com.project.vehiclevoyage.controller;

import com.project.vehiclevoyage.entities.ReviewRating;
import com.project.vehiclevoyage.entities.User;
import com.project.vehiclevoyage.service.BookingDetailsService;
import com.project.vehiclevoyage.service.ReviewRatingService;
import com.project.vehiclevoyage.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
public class ReviewController {

    @Autowired
    private ReviewRatingService reviewRatingService;
    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    private BookingDetailsService bookingDetailsService;

    @Autowired
    private VehicleService vehicleService;

    @PostMapping("/user/rate-and-review/{id}")
    public String review(@ModelAttribute ReviewRating reviewRating, @PathVariable String id, Principal principal, Model model) {

        User user = (User) userDetailsService.loadUserByUsername(principal.getName());
        try {
            // Save review
            reviewRating.setUser(user);
            // set booking details
            reviewRating.setBookingDetails(bookingDetailsService.getBookingDetailsById(id));
            // set vehicle
            reviewRating.setVehicle(vehicleService.getVehicleById(reviewRating.getBookingDetails().getVehicle().getId()));

            reviewRatingService.saveReviewRating(reviewRating);
            model.addAttribute("success", "Review submitted successfully");
            return "redirect:/user/booking-history";
        }
        catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/user/booking-details";
        }

    }
}
