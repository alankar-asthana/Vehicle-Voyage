package com.project.vehiclevoyage.controller;

import com.project.vehiclevoyage.entities.EmailRequest;
import com.project.vehiclevoyage.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/send-email")
    public ResponseEntity<String> sendEmail(@RequestBody EmailRequest request) {
        emailService.sendEmail(request.getTo(), request.getSubject(), request.getBody());
        return ResponseEntity.ok("Email sent successfully!");
    }

//    @PostMapping("/sendEmail")
//    public ResponseEntity<?> sendEmail(@RequestBody EmailRequest emailRequest) {
//        System.out.println("Email request: " + emailRequest);
//
//        boolean result =this.emailService.sendEmail(emailRequest.getTo(), emailRequest.getSubject(), emailRequest.getBody());
//        if(!result)
//            return ResponseEntity.status(500).body("Email not sent..");
//        return ResponseEntity.ok("Done..");
//    }
}
