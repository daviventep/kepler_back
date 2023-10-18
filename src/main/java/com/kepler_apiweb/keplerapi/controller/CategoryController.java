package com.kepler_apiweb.keplerapi.controller;

import com.kepler_apiweb.keplerapi.model.CategoryModel;
import com.kepler_apiweb.keplerapi.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/kepler/category")
public class CategoryController {
    @Autowired
    ICategoryService categoryService;

    @GetMapping("/")
    public ResponseEntity<List<CategoryModel>> showCategory() {
        return new ResponseEntity<List<CategoryModel>> (categoryService.listCategory(),HttpStatus.OK);
    }
}
