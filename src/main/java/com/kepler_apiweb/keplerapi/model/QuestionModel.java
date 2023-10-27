package com.kepler_apiweb.keplerapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("Question")
public class QuestionModel {
    @Id
    private int id;
    private String name;
    private String description;
    private Date creation_date;
    private Boolean with_product;
    private List<Answer> answers;
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Answer {
        private int score;
        private String comment;
        private Date creation_date;
        private int user_id;
        private int product_id;
    }
}