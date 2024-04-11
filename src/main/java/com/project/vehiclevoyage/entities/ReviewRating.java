package com.project.vehiclevoyage.entities;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Document(collection="review_rating")
public class ReviewRating {

    @Id
    private String id;
    private String review;
    private double rating;

    @DBRef
    private User user;
    @DBRef
    private BookingDetails bookingDetails;
    @DBRef
    private Vehicle vehicle;

}
