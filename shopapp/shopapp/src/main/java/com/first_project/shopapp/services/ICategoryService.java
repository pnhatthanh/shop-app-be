package com.first_project.shopapp.services;

import com.first_project.shopapp.dtos.CategoryDTO;
import com.first_project.shopapp.exceptions.DataNotFoundException;
import com.first_project.shopapp.models.Category;

import java.util.List;

public interface ICategoryService {
    List<Category> getAllCategories();
    Category getCategoryById(Long id) throws DataNotFoundException;
    Category createCategory(CategoryDTO categoryDTO) throws Exception;
    Category updateCategory(Long categoryId, CategoryDTO categoryDTO) throws DataNotFoundException;
    void deleteCategory(Long categoryId);
}
