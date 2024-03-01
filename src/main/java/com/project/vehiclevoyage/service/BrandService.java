package com.project.vehiclevoyage.service;

import com.project.vehiclevoyage.entities.Brands;
import com.project.vehiclevoyage.entities.Model;
import com.project.vehiclevoyage.repository.BrandRepoImpl;
import com.project.vehiclevoyage.repository.BrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Service
public class BrandService {
    @Autowired
    BrandRepository brandRepository;

    @Autowired
    BrandRepoImpl brandRepo;


    public Brands findByBrandName(String brand_name) {
        return brandRepository.findByBrandName(brand_name);
    }

    public List<Brands> getAllBrandsByVehicleType(String vehicle_type) {
        return brandRepository.getAllBrandsByVehicleType(vehicle_type);
    }

    public List<Brands> findAll() {
        return brandRepository.findAll();
    }
    //List of models by brand name
    public List<Model> getModelsByBrandName(String brandName) {
        return brandRepository.findByBrandName(brandName).getModels();
    }

    public List<Model> getModelsByBrandNameAndVehicleType(String brandName, String vehicleType) {
        Brands brandsList = brandRepository.findByBrandNameAndVehicleType(brandName, vehicleType);
        if (brandsList == null) {
            return Collections.emptyList();
        }
        return brandsList.getModels();}

    public Model getModelDetails(String brandName, String vehicleType, String modelName) {
        Brands brandsList = brandRepository.findByBrandNameAndVehicleType(brandName, vehicleType);
        List<Model> modelList = brandsList.getModels();
        for (Model model : modelList) {
            if (model.getModelName().equals(modelName)) {
                return model;
            }
        }
        return null;
    }
}
