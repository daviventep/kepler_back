package com.kepler_apiweb.keplerapi.service;

import com.kepler_apiweb.keplerapi.model.ProductModel;

import java.util.List;
import java.util.Optional;

public interface IProductService {
    String saveProduct(ProductModel product);
    List<ProductModel> listProduct();
    Optional<ProductModel> getProductById(int productId);
    List<ProductModel> getProductsByCategory(int categoryId);

    int getNextId();

    Optional<ProductModel> getProductByName(String name);

    String updateProduct(ProductModel product);

}
