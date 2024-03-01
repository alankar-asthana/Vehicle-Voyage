package com.project.vehiclevoyage.entities;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
@Document(collection = "brand_details")
public class Brands {
    @Id
    private String id;
    private String brandName;
    private String vehicleType;
    private List<Model> models;
}
