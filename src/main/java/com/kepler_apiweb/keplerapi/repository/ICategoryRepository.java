package com.kepler_apiweb.keplerapi.repository;

import com.kepler_apiweb.keplerapi.model.CategoryModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface ICategoryRepository extends MongoRepository<CategoryModel, String> {

}
