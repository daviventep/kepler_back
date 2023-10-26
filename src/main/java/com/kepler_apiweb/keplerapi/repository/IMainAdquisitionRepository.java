package com.kepler_apiweb.keplerapi.repository;

import com.kepler_apiweb.keplerapi.model.MainAdquisitionModel;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface IMainAdquisitionRepository extends MongoRepository<MainAdquisitionModel, Integer> {
    @Query(value = "{'user_id':  ?0}")
    List<MainAdquisitionModel> findByUserId(int userId);
    @Query(value = "{'user_id':  ?0, 'status': ?1}")
    List<MainAdquisitionModel> findByUserIdStatus(int userId, String statusType);
    @Query(value = "{}", sort = "{'_id' : -1}", fields = "{'_id': 1}")
    List<MainAdquisitionModel> findLastMainAdquisition();
}