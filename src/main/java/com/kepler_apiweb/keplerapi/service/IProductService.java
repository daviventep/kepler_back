package com.kepler_apiweb.keplerapi.service;

import com.kepler_apiweb.keplerapi.DTO.ProductToStockDTO;
import com.kepler_apiweb.keplerapi.DTO.ProductWithCategoryDTO;
import com.kepler_apiweb.keplerapi.model.ProductModel;

import java.util.List;
import java.util.Optional;

public interface IProductService {
    String saveProduct(ProductModel product);
    List<ProductWithCategoryDTO> listProduct();

    // SOLUCIÓN A PREGUNTA 3
    List<ProductToStockDTO> listProductToStock();

    Optional<ProductModel> getProductById(int productId);
    Optional<ProductWithCategoryDTO> getProductByIdMap(int productId);
    List<ProductWithCategoryDTO> getProductsByCategory(int categoryId);

    int getNextId();

    Optional<ProductModel> getProductByName(String name);

    String updateProduct(ProductModel product);


}
