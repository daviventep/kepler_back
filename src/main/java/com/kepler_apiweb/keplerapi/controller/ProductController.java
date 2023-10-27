package com.kepler_apiweb.keplerapi.controller;

import com.kepler_apiweb.keplerapi.DTO.ProductWithCategoryDTO;
import com.kepler_apiweb.keplerapi.exception.ResourceExist;
import com.kepler_apiweb.keplerapi.exception.ResourceNotFoundException;
import com.kepler_apiweb.keplerapi.model.CategoryModel;
import com.kepler_apiweb.keplerapi.model.ProductModel;
import com.kepler_apiweb.keplerapi.service.ICategoryService;
import com.kepler_apiweb.keplerapi.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/kepler/product")
public class ProductController {
    @Autowired
    ICategoryService categoryService;
    @Autowired
    IProductService productService;

    @PostMapping("/")
    public ResponseEntity<String> createProduct(@RequestBody ProductModel product) {
        if (product.get_id() == 0) {
            throw new ResourceNotFoundException(String.format("¡Error! No se recibió un Id del producto."));
        }
        Boolean productExist = productService.getProductById(product.get_id()).isPresent();
        if (productExist == true) {
            int nextIdInt = productService.getNextId();
            throw new ResourceExist(String.format("El producto con iD %d ya existe, puedes usar el iD %d.",
                    product.get_id(),
                    nextIdInt));
        }
        Boolean categoryNameExist = productService.getProductByName(product.getName()).isPresent();
        if (categoryNameExist == true) {
            throw new ResourceExist(String.format("El producto con nombre %s ya existe.",
                    product.getName()));
        }
        CategoryModel category = categoryService.getCategoryById(product.getCategory_id()).
                orElseThrow(() -> new ResourceNotFoundException(String.format("¡Error! No se encontró la categoría con el Id %s.", product.getCategory_id())));
        productService.saveProduct(product);
        return new ResponseEntity<String>(productService.saveProduct(product), HttpStatus.OK);
    }

    public List<ProductWithCategoryDTO> mapProductModelToDTOList(List<ProductModel> products) {
        List<ProductWithCategoryDTO> productDTOs = new ArrayList<>();

        for (ProductModel product : products) {
            ProductWithCategoryDTO productDTO = new ProductWithCategoryDTO();
            productDTO.set_id(product.get_id());
            productDTO.setName(product.getName());
            productDTO.setMoney_unit_price(product.getMoney_unit_price());
            productDTO.setPoint_unit_price(product.getPoint_unit_price());
            productDTO.setDescription(product.getDescription());
            productDTO.setQuantity(product.getQuantity());
            productDTO.setWeight(product.getWeight());
            productDTO.setMeasure_unit(product.getMeasure_unit());
            productDTO.setStatus(product.getStatus());
            productDTO.setImage_product(product.getImage_product());
            productDTO.setCategory_id(product.getCategory_id());

            int categoryId = product.getCategory_id();
            CategoryModel category = categoryService.getCategoryById(categoryId).orElse(null);

            if (category != null) {
                productDTO.setCategory_name(category.getName());
            }

            productDTOs.add(productDTO);
        }

        return productDTOs;
    }
    @GetMapping("/")
    public ResponseEntity<List<ProductWithCategoryDTO>> showProduct() {
        List<ProductModel> products = productService.listProduct();
        List<ProductWithCategoryDTO> productDTOs = mapProductModelToDTOList(products);
        return new ResponseEntity<>(productDTOs, HttpStatus.OK);
    }
    @GetMapping("/id/{id}")
    public ResponseEntity<ProductWithCategoryDTO> filterProductById(@PathVariable int id) {
        ProductModel product = productService.getProductById(id).
                orElseThrow(() -> new ResourceNotFoundException(String.format("¡Error! No se encontró el producto con el Id %s.", id)));
        List<ProductModel> singleProductList = new ArrayList<>();
        singleProductList.add(product);
        List<ProductWithCategoryDTO> productDTOs = mapProductModelToDTOList(singleProductList);

        if (!productDTOs.isEmpty()) {
            return ResponseEntity.ok(productDTOs.get(0));
        } else {
            throw new ResourceNotFoundException(String.format("¡Error! No se encontró el producto con el Id %s.", id));
        }
    }
    @GetMapping("/category/{id}")
    public ResponseEntity<List<ProductWithCategoryDTO>> showProductByCategory(@PathVariable int id) {
        List<ProductModel> products = productService.getProductsByCategory(id);
        List<ProductWithCategoryDTO> productDTOs = mapProductModelToDTOList(products);
        return new ResponseEntity<>(productDTOs, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateProductById(@PathVariable int id, @RequestBody ProductModel detailsProduct) {
        ProductModel product = productService.getProductById(id).
                orElseThrow(() -> new ResourceNotFoundException(String.format("¡Error! No se encontró el producto con el Id %s.", id)));
        if (detailsProduct.getCategory_id() != null && !detailsProduct.getCategory_id().toString().isEmpty()) {
            CategoryModel category = categoryService.getCategoryById(product.getCategory_id()).
                    orElseThrow(() -> new ResourceNotFoundException(String.format("¡Error! No se encontró la categoría con el Id %s.", product.getCategory_id())));
            product.setCategory_id(detailsProduct.getCategory_id());
        }
        if (detailsProduct.getName() != null && !detailsProduct.getName().isEmpty()) {
            product.setName(detailsProduct.getName());
        }
        if (detailsProduct.getMoney_unit_price() != null) {
            product.setMoney_unit_price(detailsProduct.getMoney_unit_price());
        }
        if (detailsProduct.getPoint_unit_price() != null) {
            product.setPoint_unit_price(detailsProduct.getPoint_unit_price());
        }
        if (detailsProduct.getDescription() != null && !detailsProduct.getDescription().isEmpty()) {
            product.setDescription(detailsProduct.getDescription());
        }
        if (detailsProduct.getQuantity() != null) {
            product.setQuantity(detailsProduct.getQuantity());
        }
        if (detailsProduct.getWeight() != null) {
            product.setWeight(detailsProduct.getWeight());
        }
        if (detailsProduct.getMeasure_unit() != null && !detailsProduct.getMeasure_unit().isEmpty()) {
            product.setMeasure_unit(detailsProduct.getMeasure_unit());
        }
        if (detailsProduct.getStatus() != null) {
            product.setStatus(detailsProduct.getStatus());
        }
        if (detailsProduct.getImage_product() != null && !detailsProduct.getImage_product().isEmpty()) {
            product.setImage_product(detailsProduct.getImage_product());
        }
        return new ResponseEntity<String>(productService.updateProduct(product), HttpStatus.OK);
    }
}
