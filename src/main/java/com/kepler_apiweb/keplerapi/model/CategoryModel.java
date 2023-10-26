package com.kepler_apiweb.keplerapi.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document("Category")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryModel {
    @Id
    private int _id;
    private String name;
    private String description;
}
