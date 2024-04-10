package com.project.vehiclevoyage.entities;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.Date;
@Getter
@Setter
@RequiredArgsConstructor
@ToString
@Document(collection = "booking_details")
public class BookingDetails {
    @Id
    private String id;

    @DBRef
    private User user; // User ID
    @DBRef
    private Vehicle vehicle; // Vehicle ID

    private String ownerId;

    private String vehicleType; // Vehicle type
    private String bookingType; // Daily, weekly, monthly
    private String days; // Number of days
    private String weeks; // Number of weeks
    private String months; // Number of months
    private Date bookingDate; // Booking date
    private LocalDate startDate; // Start date of booking
    private LocalDate endDate; // End date of booking
    private double totalCost; // Total cost of the booking
    private String bookingStatus; // Status of the booking

    private String orderId;
    private String receiptNumber;
    private String paymentStatus; // Payment status
    private String paymentMethod; // Payment method
    private String paymentId;

    private String additionalNotes; // Additional notes
    private String pickupLocation; // Pickup location
    private String city;// City
    private String dropOffLocation; // Drop-off location
    private Date createdAt; // Created timestamp
    private Date updatedAt; // Last updated timestamp

    private String otp;

    // Getters and setters

    // Constructors
}
