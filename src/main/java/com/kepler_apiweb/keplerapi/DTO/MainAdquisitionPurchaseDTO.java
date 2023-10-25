package com.kepler_apiweb.keplerapi.DTO;

import lombok.Data;
import org.bson.types.ObjectId;

@Data
public class MainAdquisitionPurchaseDTO {
    private ObjectId user_id;
    private Boolean use_points;
}
