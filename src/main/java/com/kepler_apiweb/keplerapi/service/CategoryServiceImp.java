package com.kepler_apiweb.keplerapi.service;

import com.kepler_apiweb.keplerapi.model.CategoryModel;
import com.kepler_apiweb.keplerapi.repository.ICategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImp implements ICategoryService{
    @Autowired
    ICategoryRepository categoryRepository;


    @Override
    public List<CategoryModel> listCategory() {
        return categoryRepository.findAll();
    }
}
