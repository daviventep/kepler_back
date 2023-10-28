package com.kepler_apiweb.keplerapi.repository;

import com.kepler_apiweb.keplerapi.DTO.MainAdquisitionCompleteDTO;
import com.kepler_apiweb.keplerapi.DTO.ProductToStockDTO;
import com.kepler_apiweb.keplerapi.model.CategoryModel;
import com.kepler_apiweb.keplerapi.model.ProductModel;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Date;
import java.util.List;

public interface IProductRepository extends MongoRepository<ProductModel, Integer> {
    @Query(value = "{'category_id':  ?0}")
    List<ProductModel> findByCategoryId(int categoryId);
    @Query(value = "{}", sort = "{'_id' : -1}", fields = "{'_id': 1}")
    List<ProductModel> findLastProduct();
    ProductModel findByNameEquals(String name);
    // SOLUCIÓN A PREGUNTA 3
    @Aggregation({
            "{ $lookup: { from: 'Category', localField: 'category_id', foreignField: '_id', as: 'category' } }",
            "{ $unwind: '$category' }",
            "{ $project: { _id: 1, name: 1, quantity: 1, category_id: 1, " +
                    " category_name: '$category.name'} }",
            "{ $sort: { category_name: 1 } }"
    })
    List<ProductToStockDTO> findProductsToStock();
}