package com.kepler_apiweb.keplerapi.service;

import com.kepler_apiweb.keplerapi.DTO.ProductToStockDTO;
import com.kepler_apiweb.keplerapi.DTO.ProductWithCategoryDTO;
import com.kepler_apiweb.keplerapi.model.ProductModel;
import com.kepler_apiweb.keplerapi.repository.ICategoryRepository;
import com.kepler_apiweb.keplerapi.repository.IProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImp implements IProductService {
    @Autowired
    ICategoryRepository categoryRepository;
    @Autowired
    IProductRepository productRepository;
    @Autowired
    private MongoTemplate
            mongoTemplate;
    // Crear producto
    @Override
    public String saveProduct(ProductModel product) {
        productRepository.save(product);
        return String.format("El producto %s ha sido creado con el Id %s.", product.getName(), product.get_id());
    }

    public List<ProductWithCategoryDTO> listProductTemplate(List<Criteria> matchCriterias) {
        Criteria[] criteriaArray = matchCriterias.toArray(new Criteria[0]);
        Aggregation aggregation;

        AggregationOperation lookupOperation = Aggregation.lookup("Category", "category_id", "_id", "categoryData");
        AggregationOperation unwindOperation = Aggregation.unwind("$categoryData");
        AggregationOperation projectOperation = Aggregation.project()
//                .andExclude("_id")
                .and("name").as("name")
                .and("money_unit_price").as("money_unit_price")
                .and("point_unit_price").as("point_unit_price")
                .and("description").as("description")
                .and("quantity").as("quantity")
                .and("weight").as("weight")
                .and("measure_unit").as("measure_unit")
                .and("status").as("status")
                .and("image_product").as("image_product")
                .and("categoryData.name").as("category_name")
                .and("category_id").as("category_id");
        if (criteriaArray.length > 0) {
            aggregation = Aggregation.newAggregation(lookupOperation, unwindOperation,
                    Aggregation.match(new Criteria().andOperator(criteriaArray)), projectOperation);
        } else {
            aggregation = Aggregation.newAggregation(lookupOperation, unwindOperation, projectOperation);
        }
        AggregationResults<ProductWithCategoryDTO> results = mongoTemplate.aggregate(aggregation, "Product", ProductWithCategoryDTO.class);
        List<ProductWithCategoryDTO> resultList = results.getMappedResults();

        return resultList;
    }
//     Listar productos
    @Override
    public List<ProductWithCategoryDTO> listProduct() {
        List<Criteria> matchCriterias = new ArrayList<>();
        List<ProductWithCategoryDTO> products = listProductTemplate(matchCriterias);
         return products;
    }
    // SOLUCIÓN A PREGUNTA 3
    @Override
    public List<ProductToStockDTO> listProductToStock() {
        List<ProductToStockDTO> products = productRepository.findProductsToStock();
         return products;
    }

    // Filtrar un producto por Id
    @Override
    public Optional<ProductModel> getProductById(int productId) {
        return productRepository.findById(productId);
    }
    // Filtrar un producto por Id
    @Override
    public Optional<ProductWithCategoryDTO> getProductByIdMap(int productId) {
        List<Criteria> matchCriterias = new ArrayList<>();
        matchCriterias.add(Criteria.where("_id").is(productId));
        List<ProductWithCategoryDTO> products = listProductTemplate(matchCriterias);

        if (!products.isEmpty()) {
            return Optional.of(products.get(0));
        } else {
            return Optional.empty();
        }
    }
    // Filtrar productos por categoría
    @Override
    public List<ProductWithCategoryDTO> getProductsByCategory(int categoryId) {
        List<Criteria> matchCriterias = new ArrayList<>();
        matchCriterias.add(Criteria.where("category_id").is(categoryId));
        List<ProductWithCategoryDTO> products = listProductTemplate(matchCriterias);
        return products;
//        return productRepository.findByCategoryId(categoryId);
    }
    @Override
    public int getNextId() {
        int return_num;
        List<ProductModel> listProducts = productRepository.findLastProduct();
        System.out.println(listProducts);
        if (!listProducts.isEmpty() && listProducts.get(0) != null) {
            return_num = listProducts.get(0).get_id() + 1;
        } else {
            return_num = 1;
        }
        return return_num;
    }
    @Override
    public Optional<ProductModel> getProductByName(String name) {
        return Optional.ofNullable(productRepository.findByNameEquals(name));
    }
    // Actualizar producto por Id
    @Override
    public String updateProduct(ProductModel product) {
        productRepository.save(product);
        return String.format("El producto %s con Id %s se ha actualizado de forma exisosa.", product.getName(), product.get_id());
    }

}
