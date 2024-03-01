package com.project.vehiclevoyage.service;

import com.project.vehiclevoyage.entities.User;
import com.project.vehiclevoyage.entities.Vehicle;
import com.project.vehiclevoyage.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    public List<Vehicle> getVehiclesByUserId(User user) {
        return vehicleRepository.findByOwner_Id(user.getId());
    }

    public Optional<Vehicle> getVehicleById(String id) {
        return vehicleRepository.findById(id);
    }

    public Vehicle getVehicleByRegistrationNumber(String registrationNumber) {
        return vehicleRepository.findByRegistrationNumber(registrationNumber);
    }
}
