package com.project.vehiclevoyage.repository;
import com.project.vehiclevoyage.entities.Vehicle;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleRepository extends MongoRepository<Vehicle, String> {
    Vehicle findByRegistrationNumber(String registrationNumber);

    List<Vehicle> findByOwner_Id(String ownerId);

    Vehicle findByRegistrationNumberAndOwner_Id(String registrationNumber, String ownerId);

    Optional<Vehicle> findById(String id);
}
