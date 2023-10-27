package com.kepler_apiweb.keplerapi.repository;

import com.kepler_apiweb.keplerapi.model.QuestionModel;
import com.kepler_apiweb.keplerapi.model.UserModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface IUserRepository extends MongoRepository<UserModel, Integer> {
    @Query(value = "{}", sort = "{'_id' : -1}", fields = "{'_id': 1}")
    List<UserModel> findLastUser();
    UserModel findByIdentification(int identification);
}
