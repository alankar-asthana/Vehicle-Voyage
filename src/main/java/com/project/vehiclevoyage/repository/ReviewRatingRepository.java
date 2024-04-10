package com.project.vehiclevoyage.repository;

import com.project.vehiclevoyage.entities.ReviewRating;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRatingRepository extends MongoRepository<ReviewRating, String> {
}
