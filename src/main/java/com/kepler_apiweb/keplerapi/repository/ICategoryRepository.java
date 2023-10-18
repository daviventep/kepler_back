package com.kepler_apiweb.keplerapi.repository;

import com.kepler_apiweb.keplerapi.model.CategoryModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ICategoryRepository extends MongoRepository<CategoryModel, Integer> {
}
