package com.kepler_apiweb.keplerapi.model;

import com.mongodb.DBObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Document("Question")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionModel {
    @Id
    private int id;
    private String name;
    private String description;
    private Date creation_date;
    private Boolean with_product;
    private List<DBObject> answers = new ArrayList<>();
}
