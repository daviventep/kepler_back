package com.kepler_apiweb.keplerapi.service;

import com.kepler_apiweb.keplerapi.model.CategoryModel;
import com.kepler_apiweb.keplerapi.model.ProductModel;
import com.kepler_apiweb.keplerapi.repository.ICategoryRepository;
import com.kepler_apiweb.keplerapi.repository.IProductRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImp implements IProductService {
    @Autowired
    ICategoryRepository categoryRepository;
    @Autowired
    IProductRepository productRepository;
    // Crear producto
    @Override
    public String saveProduct(ProductModel product) {
        productRepository.save(product);
        return String.format("El producto %s ha sido creado con el Id %s.", product.getName(), product.get_id());
    }
    // Listar productos
    @Override
    public List<ProductModel> listProduct() {
         List<ProductModel> products = productRepository.findAll();
         return products;
    }
    // Filtrar un producto por Id
    @Override
    public Optional<ProductModel> getProductById(String productId) {
        return productRepository.findById(productId);
    }
    // Filtrar productos por categoría
    @Override
    public List<ProductModel> getProductsByCategory(String categoryId) {
        ObjectId objectId = new ObjectId(categoryId);
        return productRepository.findByCategoryId(objectId);
    }
    // Actualizar producto por Id
    @Override
    public String updateProduct(ProductModel product) {
        productRepository.save(product);
        return String.format("El producto %s con Id %s se ha actualizado de forma exisosa.", product.getName(), product.get_id());
    }
}
