package com.first_project.shopapp.repositories;

import com.first_project.shopapp.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product,Long> {
    boolean existsByNameProduct(String name);
    Page<Product> findAll(Pageable pageable);
}
