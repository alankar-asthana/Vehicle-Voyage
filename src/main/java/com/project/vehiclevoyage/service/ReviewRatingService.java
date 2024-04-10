package com.project.vehiclevoyage.service;

import com.project.vehiclevoyage.entities.ReviewRating;
import com.project.vehiclevoyage.repository.ReviewRatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewRatingService {
    @Autowired
    private ReviewRatingRepository reviewRatingRepository;

    public void saveReviewRating(ReviewRating reviewRating) {

        reviewRatingRepository.save(reviewRating);
    }
}
