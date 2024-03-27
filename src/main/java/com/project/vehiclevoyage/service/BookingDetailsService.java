package com.project.vehiclevoyage.service;

import com.project.vehiclevoyage.entities.BookingDetails;
import com.project.vehiclevoyage.repository.BookingDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BookingDetailsService {

    @Autowired
    private BookingDetailsRepository bookingDetailsRepository;
    public List<BookingDetails> findBookedVehicles(String city, String vehicleType, LocalDate startDate, LocalDate endDate) {
        return bookingDetailsRepository.findOverlappingBookings(city, vehicleType, startDate, endDate);
    }
}
