package com.kepler_apiweb.keplerapi.repository;

import com.kepler_apiweb.keplerapi.model.QuestionModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface IQuestionRepository extends MongoRepository<QuestionModel, Integer> {
    @Query(value = "{}", sort = "{'_id' : -1}", fields = "{'_id': 1}")
    List<QuestionModel> findLastQuestion();
    QuestionModel findByNameEquals(String name);
}
