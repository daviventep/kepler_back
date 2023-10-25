package com.kepler_apiweb.keplerapi.service;

import com.kepler_apiweb.keplerapi.model.CategoryModel;
import com.kepler_apiweb.keplerapi.repository.ICategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImp implements ICategoryService{
    @Autowired
    ICategoryRepository categoryRepository;

    @Override
    public String saveCategory(CategoryModel Category) {
        categoryRepository.save(Category);
        return String.format("La categoría %s ha sido creada.", Category.getName());
    }
    @Override
    public List<CategoryModel> listCategory() {
        return categoryRepository.findAll();
    }
    @Override
    public Optional<CategoryModel> getCategoryById(String categoryId) {
        return categoryRepository.findById(categoryId);
    }

    @Override
    public String updateCategory(CategoryModel Category) {
        categoryRepository.save(Category);
        return String.format("La categoría %s se ha actualizado de forma exisosa.", Category.get_id());
    }
}
