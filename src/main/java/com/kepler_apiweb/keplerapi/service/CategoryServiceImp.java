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
    public String saveCategory(CategoryModel category) {
        categoryRepository.save(category);
        return String.format("La categoría %s ha sido creada.", category.getName());
    }
    @Override
    public List<CategoryModel> listCategory() {
        return categoryRepository.findAll();
    }
    @Override
    public Optional<CategoryModel> getCategoryById(int categoryId) {
        return categoryRepository.findById(categoryId);
    }
    @Override
    public Optional<CategoryModel> getCategoryByName(String name) {
        return Optional.ofNullable(categoryRepository.findByNameEquals(name));
    }

    @Override
    public int getNextId() {
        int return_num;
        List<CategoryModel> listCategories = categoryRepository.findLastCategory();
        if (!listCategories.isEmpty() && listCategories.get(0) != null) {
            return_num = listCategories.get(0).get_id() + 1;
        } else {
            return_num = 1;
        }
        return return_num;
    }

    @Override
    public String updateCategory(CategoryModel category) {
        categoryRepository.save(category);
        return String.format("La categoría %s se ha actualizado de forma exisosa.", category.get_id());
    }
}
