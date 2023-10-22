package com.kepler_apiweb.keplerapi.repository;

import com.kepler_apiweb.keplerapi.model.ProductModel;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface IProductRepository extends MongoRepository<ProductModel, String> {
    @Query(value = "{'category_id':  ?0}")
    List<ProductModel> findByCategoryId(ObjectId objectId);
}
