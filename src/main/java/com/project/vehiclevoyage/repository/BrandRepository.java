package com.project.vehiclevoyage.repository;

import com.project.vehiclevoyage.entities.Brands;
import com.project.vehiclevoyage.entities.Model;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BrandRepository extends MongoRepository<Brands, String> {

    Brands findByBrandName(String brandName);

    List<Brands> getAllBrandsByVehicleType(String vehicleType);

    Brands findByBrandNameAndVehicleType(String brandName, String vehicleType);

    List<Model> getModelsByBrandName(String brandName);

    List<Brands> findAll();

    //List of models by brand name and vehicle type
    List<Model> getModelsByBrandNameAndVehicleType(String brandName, String vehicleType);
}