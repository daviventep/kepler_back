package com.kepler_apiweb.keplerapi.repository;

import com.kepler_apiweb.keplerapi.model.CategoryModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ICategoryRepository extends MongoRepository<CategoryModel, Integer> {
    @Query(value = "{}", sort = "{'_id' : -1}", fields = "{'_id': 1}")
    List<CategoryModel> findLastCategory();
    CategoryModel findByNameEquals(String name);
}
