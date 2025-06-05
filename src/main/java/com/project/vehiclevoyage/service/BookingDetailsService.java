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

    public List<BookingDetails> getBookingDetailsByVehicleId(String id) {
        return bookingDetailsRepository.findByVehicleId(id);
    }

    public List<BookingDetails> getBookingDetailsByUserId(String id) {
        return bookingDetailsRepository.findByUserId(id);
    }

    public String generateOtp() {
        return String.format("%06d", (int) (Math.random() * 999999));
    }

    public List<BookingDetails> getBookingDetailsByOwnerId(String id) {
        return bookingDetailsRepository.findByOwnerId(id);
    }

    public BookingDetails getBookingDetailsById(String bookingDetailsId) {
        return bookingDetailsRepository.findById(bookingDetailsId).get();
    }

    public List<BookingDetails> getBookings() {
        return bookingDetailsRepository.findAll();
    }

    public BookingDetails getBookingDetailsForOrderCreation(String id) {
        List <BookingDetails> bookingDetailsList = (List<BookingDetails>) bookingDetailsRepository.findByVehicleId(id);
        //Fetch the latest booking detail
        if(bookingDetailsList.size() == 0) {
            return null;
        }
        return bookingDetailsList.get(bookingDetailsList.size() - 1);
    }
}
