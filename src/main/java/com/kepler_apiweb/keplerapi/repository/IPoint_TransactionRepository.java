package com.kepler_apiweb.keplerapi.repository;

import com.kepler_apiweb.keplerapi.model.CategoryModel;
import com.kepler_apiweb.keplerapi.model.PointTransactionModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface IPoint_TransactionRepository extends MongoRepository<PointTransactionModel, Integer> {
    @Query(value = "{}", sort = "{'_id' : -1}", fields = "{'_id': 1}")
    List<PointTransactionModel> findLastPointTransaction();
}
