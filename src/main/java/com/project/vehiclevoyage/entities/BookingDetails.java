package com.project.vehiclevoyage.entities;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
@Getter
@Setter
@RequiredArgsConstructor
@ToString
@Document(collection = "booking_details")
public class BookingDetails {
    @Id
    private String id;
    private String bookingId;
    private String userId;
    private Date bookingDate;
    private Date pickupDate;
    private Date returnDate;
}
