package com.project.vehiclevoyage.entities;

import jakarta.annotation.Generated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
@Document(collection = "vehicle")
public class Vehicle{
    @Id
    private String id;
    @NotBlank(message = "Brand name cannot be blank")
    private String brandName;
    @NotBlank(message = "Vehicle type cannot be blank")
    private String vehicleType;
    @NotBlank(message = "Model name cannot be blank")
    private String modelName;
    @NotBlank(message = "Model type cannot be blank")
    private String modelType;
    @NotBlank(message = "Registration number cannot be blank")
    @Pattern(regexp = "^[A-Z]{2}[\\\\ -]{0,1}[0-9]{2}[\\\\ -]{0,1}[A-Z]{1,2}[\\\\ -]{0,1}[0-9]{4}$", message = "Invalid registration number format")
    @Indexed(unique = true)
    private String registrationNumber;
    @NotBlank(message = "Registration date cannot be blank")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date registrationDate;
    @NotBlank(message = "Engine cc cannot be blank")
    private long engineCC;
    @NotBlank(message = "Mileage cannot be blank")
    private double mileage;
    @NotBlank(message = "Seating capacity cannot be blank")
    private int seatingCapacity;
    @NotBlank(message = "Distance travelled cannot be blank")
    private double distanceTravelled;
    @NotBlank(message = "Color cannot be blank")
    private String color;
    @NotBlank(message = "Fuel type cannot be blank")
    private String fuelType;
    @NotBlank(message = "Transmission type cannot be blank")
    private String transmissionType;
    @NotBlank(message = "Owner aadhar number cannot be blank")
    @Pattern(regexp = "^[0-9]{12}$", message = "Invalid owner aadhar number format")
    private String ownerAadharNumber;
    @NotBlank(message = "Owner contact number cannot be blank")
    @Pattern(regexp = "^[0-9]{10}$", message = "Invalid owner contact number format")
    private String ownerContactNumber;
    @NotBlank(message = "Owner address cannot be blank")
    private String houseNumber_shopNumber;
    @NotBlank(message = "Owner address cannot be blank")
    private String streetName_streetNumber;
    @NotBlank(message = "Owner address cannot be blank")
    private String colony;
    @NotBlank(message = "Owner address cannot be blank")
    private String country;
    @NotBlank(message = "Owner address cannot be blank")
    private String state;
    @NotBlank(message = "Owner address cannot be blank")
    private String city;
    @NotBlank(message = "Owner address cannot be blank")
    @Pattern(regexp = "^[1-9]{1}[0-9]{2}\\s{0,1}[0-9]{3}$", message = "Invalid owner pincode format")
    private String pincode;
    @NotBlank(message = "Registration certificate cannot be blank")
    private String registrationCertificateImage;
    @NotBlank(message = "Front view image cannot be blank")
    private String frontViewImage;
    @NotBlank(message = "Back view image cannot be blank")
    private String backViewImage;
    @NotBlank(message = "Left view image cannot be blank")
    private String leftViewImage;
    @NotBlank(message = "Right view image cannot be blank")
    private String rightViewImage;

    private boolean isAvailable = true;
    private boolean isDeleted = false;
    private Date createdDate=new Date();
    private Date updatedDate=new Date();
    @DBRef
    private User owner;

    public void setOwner(User owner) {
        this.owner = owner;
    }
}
