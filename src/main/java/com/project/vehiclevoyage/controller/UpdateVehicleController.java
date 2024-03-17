package com.project.vehiclevoyage.controller;

import com.project.vehiclevoyage.entities.User;
import com.project.vehiclevoyage.entities.Vehicle;
import com.project.vehiclevoyage.helper.Message;
import com.project.vehiclevoyage.repository.VehicleRepository;
import com.project.vehiclevoyage.service.UserDetailsServiceImplementation;
import com.project.vehiclevoyage.service.VehicleService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.project.vehiclevoyage.helper.ImageUploadHelper.UPLOAD_DIR;

@Controller
public class UpdateVehicleController {

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private UserDetailsServiceImplementation userDetailsService;

    @Autowired
    private VehicleRepository vehicleRepository;


    //Update vehicle details
    @GetMapping("/user/updateVehicle/{registrationNumber}")
    @PreAuthorize("hasRole('USER')")
    public String getUpdateVehicleForm(@PathVariable String registrationNumber, Model model, Principal principal) {

        User user = (User) userDetailsService.loadUserByUsername(principal.getName());
        Vehicle vehicle = vehicleService.getVehicleByRegistrationNumber(registrationNumber);

        if(!user.getId().equals((vehicle.getOwner().getId()))) {
            model.addAttribute("title", "Vehicle Details-VehicleVoyage");
            model.addAttribute("user", user);
            model.addAttribute("vehicle", vehicle);
            model.addAttribute("message", new Message("You does not own this vehicle that's why you are not authorized to update this vehicle", "alert-danger"));
            return "update_vehicle_form";
        }

        if(vehicle == null) {
            Vehicle nullvehicle = new Vehicle();
            model.addAttribute("title", "Vehicle Details-VehicleVoyage");
            model.addAttribute("user", user);
            model.addAttribute("vehicle", nullvehicle);
            model.addAttribute("message", new Message("Vehicle not found", "alert-danger"));
            return "update_vehicle_form";
        }

        Date registrationDate = vehicle.getRegistrationDate();

        // Convert date to desired format
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC")); // Set timezone if needed
        String formattedDate = dateFormat.format(registrationDate);

        // Add formatted date to the model
        model.addAttribute("registrationDate", formattedDate);

        System.out.println("Vehicle Details: " + vehicle.toString());
        model.addAttribute("title", "Vehicle Details-VehicleVoyage");
        model.addAttribute("user", user);
        model.addAttribute("vehicle", vehicle);
        return "update_vehicle_form";
    }
    @PostMapping("/user/update-vehicle-details")
    @PreAuthorize("hasRole('USER')")
    public String updateVehicleDetails(@ModelAttribute @Valid Vehicle updatedVehicle, BindingResult bindingResult,
                                       @RequestParam("registrationCertificate") MultipartFile file1,
                                       @RequestParam("frontView") MultipartFile file2, @RequestParam("backView") MultipartFile file3,
                                       @RequestParam("leftView") MultipartFile file4, @RequestParam("rightView") MultipartFile file5,
                                       Model model, Principal principal) {

        try {
            // Authorization check
            User user = (User) userDetailsService.loadUserByUsername(principal.getName());

            if (bindingResult.hasErrors()) {
                throw new Exception(bindingResult.getFieldError().getDefaultMessage());
            }

            // Fetch the existing vehicle entity from the database using its ID
            Vehicle existingVehicle = vehicleRepository.findById(updatedVehicle.getId())
                    .orElseThrow(() -> new Exception("Vehicle not found"));

            // Update other fields of the existing entity based on non-null values from the updated entity
            BeanUtils.copyProperties(updatedVehicle, existingVehicle, getNullPropertyNames(updatedVehicle));

            //processing image

            //Taking registration certificate image from vehicle model and saving it in vehicle
            if(!file1.isEmpty()) {
                //delete old image
                deleteImage(existingVehicle.getRegistrationCertificateImage());
                //upload new image
                System.out.println("Uploading Image: "+file1.getOriginalFilename());
                String name = uploadImage(file1);
                if(name != null)
                    existingVehicle.setRegistrationCertificateImage(name);
            }

            //Taking front vehicle image from vehicle model and saving it in vehicle
            if(!file2.isEmpty()) {
                deleteImage(existingVehicle.getFrontViewImage());
                String name = uploadImage(file2);
                if(name != null)
                    existingVehicle.setFrontViewImage(name);
            }

            //Taking back view image from vehicle model and saving it in vehicle
            if(!file3.isEmpty()) {
                deleteImage(existingVehicle.getBackViewImage());
                String name = uploadImage(file3);
                if(name != null)
                    existingVehicle.setBackViewImage(name);
            }

            //Taking left view image from vehicle model and saving it in vehicle
            if(!file4.isEmpty()) {
                deleteImage(existingVehicle.getLeftViewImage());
                String name = uploadImage(file4);
                if(name != null)
                    existingVehicle.setLeftViewImage(name);
            }

            //Taking right view image from vehicle model and saving it in vehicle
            if(!file5.isEmpty()) {
                deleteImage(existingVehicle.getRightViewImage());
                String name = uploadImage(file5);
                if(name != null)
                    existingVehicle.setRightViewImage(name);
            }


            // Set the updatedDate field to the current date
            existingVehicle.setUpdatedDate(new Date());

            // Save the updated entity
            Vehicle savedVehicle = vehicleRepository.save(existingVehicle);

            System.out.println("Updated Vehicle Details: " + savedVehicle.toString());

            model.addAttribute("title", "Vehicle Details-VehicleVoyage");
            model.addAttribute("user", user);
            model.addAttribute("vehicle", savedVehicle);
            model.addAttribute("message", new Message("Vehicle details updated successfully", "alert-success"));
            //redirect to vehicle details page
            return "redirect:/user/vehicle-details/" + savedVehicle.getRegistrationNumber();

        } catch (Exception e) {
            User user = (User) userDetailsService.loadUserByUsername(principal.getName());

            model.addAttribute("title", "Vehicle Details-VehicleVoyage");
            model.addAttribute("user", user);
            model.addAttribute("vehicle", updatedVehicle);
            model.addAttribute("message", new Message(e.getMessage(), "alert-danger"));
            return "vehicle_details";
        }
    }

    private void deleteImage(String image) {
        Path path = Paths.get(UPLOAD_DIR, image);
        if (Files.exists(path)) {
            try {
                Files.delete(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Utility method to save uploaded image file to a designated location
    public String uploadImage(MultipartFile file) throws IOException {
        String uniqueFileName = null;
        try {
            String fileName = file.getOriginalFilename();
            System.out.println("File Name: " + fileName);
            uniqueFileName = UUID.randomUUID().toString() + "-" + fileName;
            System.out.println("Unique File Name: " + uniqueFileName);
            Files.copy(file.getInputStream(), Paths.get(UPLOAD_DIR, uniqueFileName), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("File uploaded successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return uniqueFileName;
    }

    // Utility method to get null property names of an object
    private String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }
}
