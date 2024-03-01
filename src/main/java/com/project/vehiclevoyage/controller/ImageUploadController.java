package com.project.vehiclevoyage.controller;

import com.project.vehiclevoyage.helper.ImageUploadHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/vehicle")
public class ImageUploadController {
    @Autowired
    private ImageUploadHelper imageUploadHelper;

    @PostMapping("/upload-image")
    public ResponseEntity<String> uploadImage(@RequestParam("image") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("Please select an image file to upload");
            }
            boolean uploaded = imageUploadHelper.uploadImage(file);
            if (uploaded) {
                return ResponseEntity.ok("Image uploaded successfully");
            } else {
                return ResponseEntity.badRequest().body("Failed to upload image");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}

