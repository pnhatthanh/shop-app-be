package com.first_project.shopapp.controllers;

import com.first_project.shopapp.dtos.CategoryDTO;
import com.first_project.shopapp.exceptions.DataNotFoundException;
import com.first_project.shopapp.models.Category;
import com.first_project.shopapp.services.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/categories")
public class CategoryController {
    @Autowired
    CategoryService categoryService;
    //Sửa cái cùng tên category
    @PostMapping("")
    public ResponseEntity<?> createCategroy(@Valid @RequestBody CategoryDTO categoryDTO, BindingResult result){
        if(result.hasErrors()){
            List<String> messageErrors=result.getAllErrors().stream()
                    .map(error->error.getDefaultMessage()).toList();
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid data provider");
        }
        try {
            categoryService.createCategory(categoryDTO);
            return ResponseEntity.status(HttpStatus.OK).body("Insert category successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @GetMapping("")
    public ResponseEntity<?> getAllProduct(){
        List<Category> categories=categoryService.getAllCategories();
        return ResponseEntity.status(HttpStatus.OK).body(categories);
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryDTO categoryDTO, BindingResult result){
        if(result.hasErrors()){
            List<String> messageErrors=result.getAllErrors().stream().map(error->error.getDefaultMessage()).toList();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid data provider");
        }
        try {
            Category category=categoryService.updateCategory(id,categoryDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body("Update category successfully");
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id){
        categoryService.deleteCategory(id);
        return ResponseEntity.status(HttpStatus.OK).body("Delete category successfully");
    }
}
