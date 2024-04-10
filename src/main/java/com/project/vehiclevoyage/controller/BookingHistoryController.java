package com.project.vehiclevoyage.controller;

import com.project.vehiclevoyage.entities.BookingDetails;
import com.project.vehiclevoyage.entities.User;
import com.project.vehiclevoyage.entities.Vehicle;
import com.project.vehiclevoyage.service.BookingDetailsService;
import com.project.vehiclevoyage.service.UserDetailsServiceImplementation;
import com.project.vehiclevoyage.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class BookingHistoryController {

    @Autowired
    private UserDetailsServiceImplementation userDetailsService;

    @Autowired
    private BookingDetailsService bookingDetailsService;

    @Autowired
    private VehicleService vehicleService;

    @GetMapping("/user/booking-history")
    @PreAuthorize("hasRole('USER')")
    public String getBookingHistoryPage(Model model, Principal principal) {

        System.out.println("Opening booking history page for user: " + principal.getName());
        User user = (User) userDetailsService.loadUserByUsername(principal.getName());

        //Fetch list of all those booking details where either user id is matched or vehicle id of its own vehicle is matched
        List<BookingDetails> bookingDetailsByUserId = bookingDetailsService.getBookingDetailsByUserId(user.getId());

        //Fetch list of all those booking details where user id is matched with owner id
        List<BookingDetails> bookingDetailsByOwnerId = bookingDetailsService.getBookingDetailsByOwnerId(user.getId());

        //Combine both lists to get all booking details without duplicates
        List<BookingDetails> bookingDetailsList = new ArrayList<>();
        bookingDetailsList.addAll(bookingDetailsByUserId);
        bookingDetailsList.addAll(bookingDetailsByOwnerId);

        //Sort the list of booking details by booking date
        bookingDetailsList.sort((a, b) -> a.getBookingDate().compareTo(b.getBookingDate()));
        //Print the list of booking details
        for (BookingDetails bookingDetail : bookingDetailsList) {
            System.out.println("Booking History: "+bookingDetail.getId());
        }

        model.addAttribute("user", user);
        model.addAttribute("bookingDetailsList", bookingDetailsList);
        return "booking_history";
    }

    @GetMapping("/user/confirm-pickup/{id}")
    public String showPickupConfirmationForm(@PathVariable("id") String bookingDetailsId, Model model) {
        BookingDetails bookingDetails = bookingDetailsService.getBookingDetailsById(bookingDetailsId);
        model.addAttribute("bookingDetails", bookingDetails);
        return "pickup-confirmation-form";
    }

    @PostMapping("/user/confirm-pickup/{id}")
    public String confirmPickup(@PathVariable("id") String bookingDetailsId, @RequestParam("otp") String enteredOTP, Model model) {

        BookingDetails bookingDetails = bookingDetailsService.getBookingDetailsById(bookingDetailsId);
        try {
            //fetch booking details

            boolean flag = false;
            //Compare entered OTP with booking details otp
            if (enteredOTP.equals(bookingDetails.getOtp())) {
                //Generate new Otp
                String newOtp = bookingDetailsService.generateOtp();
                bookingDetails.setOtp(newOtp);
                bookingDetails.setBookingStatus("Picked-Up");
                bookingDetailsService.saveBookingDetails(bookingDetails);
                flag = true;
            }
            if (flag) {
                return "redirect:/user/booking-history";
            } else {
                throw new Exception("Invalid OTP");
            }
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("bookingDetails", bookingDetails);
            return "pickup-confirmation-form";
        }
    }

    @GetMapping("/user/confirm-drop-off/{id}")
    public String showDropOffConfirmationForm(@PathVariable("id") String bookingDetailsId, Model model) {
        BookingDetails bookingDetails = bookingDetailsService.getBookingDetailsById(bookingDetailsId);
        model.addAttribute("bookingDetails", bookingDetails);
        return "drop-off-confirmation-form";
    }

    @PostMapping("/user/confirm-drop-off/{id}")
    public String confirmDropOff(@PathVariable("id") String bookingDetailsId, @RequestParam("otp") String enteredOTP, Model model) {
        BookingDetails bookingDetails = bookingDetailsService.getBookingDetailsById(bookingDetailsId);
        try {
            //Compare entered OTP with booking details otp
            if (enteredOTP.equals(bookingDetails.getOtp())) {
                bookingDetails.setOtp(null);
                bookingDetails.setBookingStatus("Dropped-Off");
                bookingDetailsService.saveBookingDetails(bookingDetails);
                return "redirect:/user/booking-history";
            } else {
                throw new Exception("Invalid OTP");
            }
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("bookingDetails", bookingDetails);
            return "drop-off-confirmation-form";
        }
    }
}
