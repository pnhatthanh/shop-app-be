package com.first_project.shopapp.repositories;

import com.first_project.shopapp.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Long> {
    boolean existsByNameCategory(String nameCategory);
}
