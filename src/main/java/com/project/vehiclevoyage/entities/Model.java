package com.project.vehiclevoyage.entities;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
@Component
public class Model {
    private String modelName;
    private String modelType;
    private int seatingCapacity;
}
