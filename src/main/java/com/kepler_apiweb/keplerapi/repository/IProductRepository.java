package com.kepler_apiweb.keplerapi.repository;

import com.kepler_apiweb.keplerapi.model.CategoryModel;
import com.kepler_apiweb.keplerapi.model.ProductModel;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface IProductRepository extends MongoRepository<ProductModel, Integer> {
    @Query(value = "{'category_id':  ?0}")
    List<ProductModel> findByCategoryId(int categoryId);
    @Query(value = "{}", sort = "{'_id' : -1}", fields = "{'_id': 1}")
    List<ProductModel> findLastProduct();
    ProductModel findByNameEquals(String name);

//    @Query("" +
//            "    " +
//            "        $lookup: {" +
//            "            from: 'Category'," +
//            "            localField: 'category_id'," +
//            "            foreignField: '_id'," +
//            "            as: 'category'" +
//            "        }" +
//            "    }," +
//            "    {" +
//            "        $project: {" +
//            "            _id: 1," +
//            "            name: 1," +
//            "            money_unit_price: 1," +
//            "            point_unit_price: 1," +
//            "            description: 0," +
//            "            quantity: 1," +
//            "            weight: 1," +
//            "            measure_unit: 1," +
//            "            status: 1," +
//            "            image_product: 1," +
//            "            category_id: 1," +
//            "            category_name: '$category.name'  // Aquí se obtiene 'category.name'" +
//            "        }" +
//            "    }," +
//            "    {" +
//            "        $unwind: {" +
//            "            path: '$category'," +
//            "            preserveNullAndEmptyArrays: true" +
//            "        }" +
//            "    }" +
//            "]")
//    List<ProductModel> findProductsWithCategoryName();
}
