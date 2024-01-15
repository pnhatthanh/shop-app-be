package com.first_project.shopapp.services;

import com.first_project.shopapp.dtos.CategoryDTO;
import com.first_project.shopapp.exceptions.DataNotFoundException;
import com.first_project.shopapp.exceptions.InvalidParamException;
import com.first_project.shopapp.models.Category;
import com.first_project.shopapp.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService implements ICategoryService{
    @Autowired
    CategoryRepository categoryRepository;
    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getCategoryById(Long id) throws DataNotFoundException {
        return categoryRepository.findById(id).orElseThrow(
                ()->new DataNotFoundException("Category not found")
        );
    }

    @Override
    public Category createCategory(CategoryDTO categoryDTO) throws Exception {
        if(categoryRepository.existsByNameCategory(categoryDTO.getName())){
            throw new InvalidParamException("Name's category already exists");
        }
        Category category=new Category();
        category.setNameCategory(categoryDTO.getName());
        return categoryRepository.save(category);
    }

    @Override
    public Category updateCategory(Long categoryId, CategoryDTO categoryDTO) throws DataNotFoundException {
        try {
            Category category = getCategoryById(categoryId);
            category.setNameCategory(categoryDTO.getName());
            return categoryRepository.save(category);
        } catch (DataNotFoundException e) {
            throw new DataNotFoundException(e.getMessage());
        }
    }

    @Override
    public void deleteCategory(Long categoryId) {
        categoryRepository.deleteById(categoryId);
    }
}
