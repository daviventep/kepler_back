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

@Document("User")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserModel {
    @Id
    private String _id;
    private int identification;
    private String first_name;
    private String last_name;
    private String email;
    private String phone_number;
    private String address;
    private int points;
    private Double salary;
    private String password;
    private Date creation_date;
    private Date birth_date;
    private Boolean is_active;
    private List<Profiles> profiles = new ArrayList<>();
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Profiles {
        private String type;
    }
}
