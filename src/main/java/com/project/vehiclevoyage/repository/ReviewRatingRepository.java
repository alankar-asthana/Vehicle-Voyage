package com.project.vehiclevoyage.repository;

import com.project.vehiclevoyage.entities.ReviewRating;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRatingRepository extends MongoRepository<ReviewRating, String> {
    double getAverageRatingByVehicleId(String id);

    List<ReviewRating> findAllByVehicleId(String id); // <3>
}
