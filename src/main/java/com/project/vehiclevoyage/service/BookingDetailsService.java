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
    public List<BookingDetails> findBookedVehicles(String city, String vehicleType, LocalDate startDate, LocalDate endDate, String bookingStatus) {
        return bookingDetailsRepository.findOverlappingBookings(city, vehicleType, startDate, endDate, bookingStatus);
    }

    public void saveBookingDetails(BookingDetails bookingDetails) {
        bookingDetailsRepository.save(bookingDetails);
    }

    public BookingDetails getBookingDetailsByOrderId(String orderId) {
        return bookingDetailsRepository.findByOrderId(orderId);
    }

    public void deleteBookingDetailsByOrderId(String orderId) {
        bookingDetailsRepository.deleteByOrderId(orderId);
    }

    public BookingDetails getBookingDetailsByVehicleId(String id) {
        return bookingDetailsRepository.findByVehicleId(id);
    }
}
