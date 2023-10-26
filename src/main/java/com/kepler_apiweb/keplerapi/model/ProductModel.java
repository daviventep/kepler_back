package com.kepler_apiweb.keplerapi.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("Product")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductModel {
    @Id
    private int _id;
    private String name;
    private Double money_unit_price;
    private Integer point_unit_price;
    private String description;
    private Integer quantity;
    private Double weight;
    private String measure_unit;
    private Boolean status;
    private String image_product;
    private Integer category_id;
}
