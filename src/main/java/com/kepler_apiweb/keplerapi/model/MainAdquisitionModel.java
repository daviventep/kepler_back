package com.kepler_apiweb.keplerapi.model;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import com.mongodb.DBObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document("Main_Adquisition")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MainAdquisitionModel {
    @Id
    private int _id;
    private Double money_total_value;
    private Integer point_total_value;
    private Date creation_date;
    private Date delivery_date;
    private int user_id;
    private String status;
    private List<AdquisitionDetail> adquisition_details = new ArrayList<>();
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AdquisitionDetail {
        private int product_id;
        private Double money_unit_value;
        private Integer point_unit_value;
        private int quantity;
        private String description;
    }
}
