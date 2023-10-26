package com.kepler_apiweb.keplerapi.service;

import com.kepler_apiweb.keplerapi.model.CategoryModel;

import java.util.List;

public interface ICategoryService {
    String saveCategory(CategoryModel category);
    List<CategoryModel> listCategory();
}
