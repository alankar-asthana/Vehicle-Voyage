package com.project.vehiclevoyage.controller;

import com.project.vehiclevoyage.entities.User;
import com.project.vehiclevoyage.entities.Vehicle;
import com.project.vehiclevoyage.helper.Message;
import com.project.vehiclevoyage.repository.UserRepository;
import com.project.vehiclevoyage.repository.VehicleRepository;
import com.project.vehiclevoyage.service.UserDetailsServiceImplementation;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.UUID;
import static com.project.vehiclevoyage.helper.ImageUploadHelper.UPLOAD_DIR;

@Controller
public class AddVehicleController {
    @Autowired
    VehicleRepository vehicleRepository;
    @Autowired
    UserRepository userRepository;

    @Autowired
    UserDetailsServiceImplementation userDetailsService;

    User user;


    @GetMapping("/user/add-vehicle-form")
    public String registerVehicle (Model model, Principal principal) {
        user = (User) userDetailsService.loadUserByUsername(principal.getName());
        model.addAttribute("title", "Add Vehicle-VehicleVoyage");
        model.addAttribute("user", user);
        model.addAttribute("vehicle", new Vehicle());
        return "add-vehicle";
    }

    @PostMapping("/user/save_vehicle")
    public String saveVehicle(@ModelAttribute @Valid Vehicle vehicle, BindingResult bindingResult, Model model,
                              HttpSession session, Principal principal,
                              @RequestParam("registrationCertificate") MultipartFile file1,
                              @RequestParam("frontView") MultipartFile file2,
                              @RequestParam("backView") MultipartFile file3,
                              @RequestParam("leftView") MultipartFile file4,
                              @RequestParam("rightView") MultipartFile file5) {

        try {
            if (bindingResult.hasErrors()) {
                throw new Exception(bindingResult.getFieldError().getDefaultMessage());
            }
            Vehicle existingVehicle = vehicleRepository.findByRegistrationNumber(vehicle.getRegistrationNumber());
            if (existingVehicle != null) {
                model.addAttribute("vehicle", vehicle);
                throw new Exception("Vehicle already exists");
            }
            // Retrieve user from session
            user = (User) userDetailsService.loadUserByUsername(principal.getName());
            model.addAttribute("user", user);
            System.out.println("Session user: " + user);
            if(user == null) {
                throw new Exception("User not found");
            }
            // Validate owner through Aadhar and current user
            if (!vehicle.getOwnerAadharNumber().equals(user.getAadharNumber())) {
                throw new Exception("Owner Aadhar number does not match. Unauthorized access");
            } else {
                vehicle.setOwner(user);
            }
            //processing image

            //Taking registration certificate image from vehicle model and saving it in vehicle
            if(!file1.isEmpty()) {
                System.out.println("Uploading Image: "+file1.getOriginalFilename());
                String name = uploadImage(file1);
                if(name != null)
                    System.out.println("File name after upload: "+name);
                    System.out.println("Data type: "+name.getClass().getName());
                    vehicle.setRegistrationCertificateImage(name);
            } else{
                throw new Exception("Please upload registration certificate");
            }

            //Taking front vehicle image from vehicle model and saving it in vehicle
            if(!file2.isEmpty()) {
                String name = uploadImage(file2);
                if(name != null)
                    vehicle.setFrontViewImage(name);
            } else{
                throw new Exception("Please upload front vie image");
            }

            //Taking back view image from vehicle model and saving it in vehicle
            if(!file3.isEmpty()) {
                String name = uploadImage(file3);
                if(name != null)
                    vehicle.setBackViewImage(name);
            } else{
                throw new Exception("Please upload back view image");
            }

            //Taking left view image from vehicle model and saving it in vehicle
            if(file4 != null) {
                String name = uploadImage(file4);
                if(name != null)
                    vehicle.setLeftViewImage(name);
            } else{
                throw new Exception("Please upload left view image");
            }

            //Taking right view image from vehicle model and saving it in vehicle
            if(file5 != null) {
                String name = uploadImage(file5);
                if(name != null)
                    vehicle.setRightViewImage(name);
            } else{
                throw new Exception("Please upload right view image");
            }

            //Taking registration date from vehicle model and saving it in vehicle
            System.out.println("Registration Date: " + vehicle.getRegistrationDate());

            Vehicle result = this.vehicleRepository.save(vehicle);
            System.out.println("Saved Vehicle: " + result.toString());
            model.addAttribute("vehicle", new Vehicle());
            model.addAttribute("user", user);
            model.addAttribute("message", new Message("Vehicle added successfully", "alert-success"));
            return "add-vehicle";
        } catch (Exception e) {
            e.printStackTrace();
            user = (User) userDetailsService.loadUserByUsername(principal.getName());
            System.out.println(e.getMessage());
            model.addAttribute("user", user);
            model.addAttribute("vehicle", vehicle);
            model.addAttribute("message", new Message(e.getMessage(), "alert-danger"));
            return "add-vehicle";
        }
    }


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

}

/*

vehicle.setRegistrationCertificateImage(String.valueOf(rcimage.getOriginalFilename()));
                vehicle.setFrontViewImage(String.valueOf(fvimage.getOriginalFilename()));
                vehicle.setBackViewImage(String.valueOf(bvimage.getOriginalFilename()));
                vehicle.setLeftViewImage(String.valueOf(lvimage.getOriginalFilename()));
                vehicle.setRightViewImage(String.valueOf(rvimage.getOriginalFilename()));

                File file = new ClassPathResource("static/images").getFile();
                Path path1 = Paths.get(file.getAbsolutePath() + File.separator + (rcimage.getOriginalFilename()));
                Path path2 = Paths.get(file.getAbsolutePath() + File.separator + (fvimage.getOriginalFilename()));
                Path path3 = Paths.get(file.getAbsolutePath() + File.separator + (bvimage.getOriginalFilename()));
                Path path4 = Paths.get(file.getAbsolutePath() + File.separator + (lvimage.getOriginalFilename()));
                Path path5 = Paths.get(file.getAbsolutePath() + File.separator + (rvimage.getOriginalFilename()));
                Files.copy(rcimage.getInputStream(), path1, StandardCopyOption.REPLACE_EXISTING);
                Files.copy(fvimage.getInputStream(), path2, StandardCopyOption.REPLACE_EXISTING);
                Files.copy(bvimage.getInputStream(), path3, StandardCopyOption.REPLACE_EXISTING);
                Files.copy(lvimage.getInputStream(), path4, StandardCopyOption.REPLACE_EXISTING);
                Files.copy(rvimage.getInputStream(), path5, StandardCopyOption.REPLACE_EXISTING);
 */