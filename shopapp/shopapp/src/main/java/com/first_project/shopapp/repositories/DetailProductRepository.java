package com.first_project.shopapp.repositories;

import com.first_project.shopapp.models.DetailProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DetailProductRepository extends JpaRepository<DetailProduct,Long> {
   Optional<DetailProduct> findByProductId(Long productId);
}
