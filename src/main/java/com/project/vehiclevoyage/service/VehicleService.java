package com.project.vehiclevoyage.service;

import com.project.vehiclevoyage.entities.User;
import com.project.vehiclevoyage.entities.Vehicle;
import com.project.vehiclevoyage.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleService{

    @Autowired
    private VehicleRepository vehicleRepository;

    public List<Vehicle> getVehiclesByUserId(User user) {

        return vehicleRepository.findByOwner_Id(user.getId());
    }

    public Vehicle getVehicleById(String id) {

        return vehicleRepository.findById(id).get();
    }

    public Vehicle getVehicleByRegistrationNumber(String registrationNumber) {
        return vehicleRepository.findByRegistrationNumber(registrationNumber);
    }

    public List<Vehicle> getVehicleByCity(String city) {
        return vehicleRepository.findByCity(city);
    }

    public List<Vehicle> getVehicleByCityAndIsAvailable(String city, boolean isAvailable) {
        return vehicleRepository.findByCityAndIsAvailable(city, isAvailable);
    }

    public List<Vehicle> findByCity(String city) {

        return vehicleRepository.findByCity(city);
    }


    public List<Vehicle> findByCityAndVehicleType(String city, String vehicleType) {

        return vehicleRepository.findByCityAndVehicleType(city, vehicleType);
    }

    public List<String> getVehicleIdsByUserId(String id) {

        return vehicleRepository.getVehicleIdsByOwnerId(id);
    }

    public List<String> getVehicleIdsByOwnerId(String id) {

        return vehicleRepository.getVehicleIdsByOwnerId(id);
    }

    public List<Vehicle> getVehicles() {
        return vehicleRepository.findAll();
    }
}
