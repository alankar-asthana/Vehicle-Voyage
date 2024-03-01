package com.project.vehiclevoyage.controller;

import com.project.vehiclevoyage.entities.User;
import com.project.vehiclevoyage.helper.Message;
import com.project.vehiclevoyage.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/registration")
public class RegistrationController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping
    public String registration(@ModelAttribute @Valid User user , BindingResult bindingResult, @RequestParam(value = "agreement", defaultValue = "false") boolean agreement, Model model, HttpSession session) {
        // Registration logic
        try {
            if(!agreement) {
                System.out.println("You have not agreed the terms and conditions");
                throw new Exception("You have not agreed the terms and conditions");
            }
            if(user.getEmail().isEmpty() || user.getPassword().isEmpty() || user.getFirstName().isEmpty() || user.getLastName().isEmpty() || user.getContactNumber().isEmpty()) {
                model.addAttribute("user", user);
                throw new Exception("Please fill all the fields");
            }
            User existingUser = userRepository.findByEmail(user.getEmail());
            if (existingUser != null) {
                bindingResult.rejectValue("email", "unique.email", "Email already exists");
            }
            if (bindingResult.hasErrors()) {
                System.out.println(bindingResult.getFieldError().getField() + " " + bindingResult.getFieldError().getDefaultMessage());
                model.addAttribute("user", user);
                throw new Exception(bindingResult.getFieldError().getDefaultMessage());
            }
//            System.out.println("User: " + user.toString());
            //password encryption
            user.setPassword(this.passwordEncoder.encode(user.getPassword())); //encode password
            User result = this.userRepository.save(user);
            System.out.println("User: " + user.toString());

            return "redirect:/login";
        }catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            model.addAttribute("user", user);
            //model.addAttribute("error", e.getMessage());
            model.addAttribute("message", new Message(e.getMessage(), "alert-danger"));
            return "signup";
        }
    }

    @GetMapping("/current-user")
    public String getLoggedInUser(Principal principal){
        return principal.getName();
    }
}




//----------------- Validation Conditions --------------//
//            if(!user.getEmail().matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")) {
//                    model.addAttribute("user", user);
//                    bindingResult.rejectValue("email", "invalid.email", "Invalid email format1");
//                    }
//            if(!user.getPassword().matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$")) {
//                model.addAttribute("user", user);
//                bindingResult.rejectValue("password", "invalid.password", "Password must contain at least one lowercase, uppercase, number, and special character");
//            }
//            if (user.getContactNumber().length() != 10 || !user.getContactNumber().matches("^[0-9]{10}$") || user.getContactNumber().startsWith("0") ) {
//                bindingResult.rejectValue("contactNumber", "invalid.contactNumber", "Invalid contact number format");
//            }
//            if (user.getAadharNumber().length() != 12 || !user.getAadharNumber().matches("^[0-9]{12}$") || user.getAadharNumber().startsWith("0") ) {
//                bindingResult.rejectValue("aadharNumber", "invalid.aadharNumber", "Invalid Aadhar number format");
//            }
//            if (!user.getLicenseNumber().matches("^(([A-Z]{2}[0-9]{2})( )|([A-Z]{2}-[0-9]{2}))((19|20)[0-9][0-9])[0-9]{7}$") || user.getLicenseNumber().startsWith("0") ) {
//                bindingResult.rejectValue("licenseNumber", "invalid.licenseNumber", "Invalid license number format");
//            }