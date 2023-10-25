package com.kepler_apiweb.keplerapi.repository;

import com.kepler_apiweb.keplerapi.model.PointTransactionModel;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IPoint_TransactionRepository extends MongoRepository<PointTransactionModel, ObjectId> {

}
