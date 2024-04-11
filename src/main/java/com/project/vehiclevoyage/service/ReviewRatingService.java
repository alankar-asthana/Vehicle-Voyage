package com.project.vehiclevoyage.service;

import com.project.vehiclevoyage.entities.ReviewRating;
import com.project.vehiclevoyage.repository.ReviewRatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewRatingService {
    @Autowired
    private ReviewRatingRepository reviewRatingRepository;

    public void saveReviewRating(ReviewRating reviewRating) {

        reviewRatingRepository.save(reviewRating);
    }

    public double getAverageRatingByVehicleId(String id) {
        List<ReviewRating> reviewRatings = reviewRatingRepository.findAllByVehicleId(id);
        return reviewRatings.stream().mapToDouble(ReviewRating::getRating).average().orElse(0.0);
    }

    public List<ReviewRating> getFeedbacks() {

        return reviewRatingRepository.findAll();
    }
}
