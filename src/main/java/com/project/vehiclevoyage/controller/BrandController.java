package com.project.vehiclevoyage.controller;

import com.project.vehiclevoyage.entities.Brands;
import com.project.vehiclevoyage.entities.Model;
import com.project.vehiclevoyage.entities.User;
import com.project.vehiclevoyage.entities.Vehicle;
import com.project.vehiclevoyage.service.BrandService;
import com.project.vehiclevoyage.service.UserDetailsServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import java.security.Principal;
import java.util.List;

@Controller
public class BrandController {

    @Autowired
    private UserDetailsServiceImplementation userDetailsService;

    @Autowired
    private BrandService brandService;

    @ModelAttribute
    public void addUser(org.springframework.ui.Model model, Principal principal) {
        User user = (User) userDetailsService.loadUserByUsername(principal.getName());
        model.addAttribute("user", user);
        System.out.println("User: " + user);
    }


    //Retrieve all brands by vehicle type
    @GetMapping("/user/retrieve-brand/{vehicleType}")
    public ResponseEntity<List<Brands>> retrieveBrand(@PathVariable String vehicleType) {
        List<Brands> brands = brandService.getAllBrandsByVehicleType(vehicleType);

        if (brands.isEmpty()) {
            return ResponseEntity.notFound().build(); // Return 404 if no brands are found for the given vehicleType
        }
        return ResponseEntity.ok(brands); // Return 200 OK along with the list of brands
    }

    //Retrieve all models by brand name and vehicle type
    @GetMapping("user/retrieve-model/{brandName}/{vehicleType}")
    public ResponseEntity<List<Model>> getModelsByBrandAndVehicleType(@PathVariable String brandName, @PathVariable String vehicleType) {
        List<Model> models = brandService.getModelsByBrandNameAndVehicleType(brandName, vehicleType);
        if (models.isEmpty()) {
            return ResponseEntity.notFound().build(); // Return 404 if no models are found
        } else {
            return ResponseEntity.ok(models); // Return 200 OK along with the list of models
        }
    }

    //Retrieve model details by brand name, vehicle type and model name
    @GetMapping("user/retrieve-model-details/{brandName}/{vehicleType}/{modelName}")
    public ResponseEntity<Model> getModelDetails(@PathVariable String brandName, @PathVariable String vehicleType, @PathVariable String modelName) {
        Model model = brandService.getModelDetails(brandName, vehicleType, modelName);
        if (model == null) {
            return ResponseEntity.notFound().build(); // Return 404 if no model is found
        } else {
            return ResponseEntity.ok(model); // Return 200 OK along with the model details
        }
    }
}
