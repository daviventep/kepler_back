package com.kepler_apiweb.keplerapi.model;

import com.mongodb.DBObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document("Point_Transaction")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PointTransactionModel {
    @Id
    private ObjectId id;
    private int quantity_point;
    private String action;
    private List<DBObject> adquisition = new ArrayList<>();

}
