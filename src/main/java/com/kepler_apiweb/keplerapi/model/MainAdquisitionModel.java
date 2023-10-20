package com.kepler_apiweb.keplerapi.model;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import com.mongodb.DBObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document("MainAdquisitionModel")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class MainAdquisitionModel {
    @Id
    private Double money_total_value;
    private int point_total_value;
    private Date creation_date;
    private Date delivery_date;
    private String user_id;
    private String status;
    private List<DBObject> adquisiton_details = new ArrayList<>();
}
