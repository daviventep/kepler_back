package com.kepler_apiweb.keplerapi.repository;

import com.kepler_apiweb.keplerapi.model.MainAdquisitionModel;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface IMainAdquisitionRepository extends MongoRepository<MainAdquisitionModel, String> {
    @Query(value = "{'user_id':  ?0}")
    List<MainAdquisitionModel> findByUserId(ObjectId objectId);
    @Query(value = "{'user_id':  ?0, 'status': ?1}")
    List<MainAdquisitionModel> findByUserIdStatus(ObjectId objectId, String statusType);
}