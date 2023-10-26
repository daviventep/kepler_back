package com.kepler_apiweb.keplerapi.repository;

import com.kepler_apiweb.keplerapi.model.QuestionModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface QuestionRepository extends MongoRepository<QuestionModel, String> {
}

