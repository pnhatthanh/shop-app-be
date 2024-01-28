package com.first_project.shopapp.repositories;

import com.first_project.shopapp.models.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductImageRepository extends JpaRepository<ProductImage,Long> {
    List<ProductImage> findByProductId(Long productId);
    //Optional<ProductImage> findByImageUrl(String imageUrl);
}
