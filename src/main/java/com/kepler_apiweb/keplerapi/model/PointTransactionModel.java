package com.kepler_apiweb.keplerapi.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Document("Point_Transaction")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PointTransactionModel {
    @Id
    private int _id;
    private int quantity_point;
    private String action;
    private Adquisition adquisition;
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Adquisition {
        private int user_id;
        private int main_adquisition_id;
        private Date transaction_date;
    }
}
