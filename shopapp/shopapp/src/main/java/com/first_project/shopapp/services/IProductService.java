package com.first_project.shopapp.services;

import com.first_project.shopapp.dtos.ProductDTO;
import com.first_project.shopapp.dtos.ProductImageDTO;
import com.first_project.shopapp.exceptions.DataNotFoundException;
import com.first_project.shopapp.models.Product;
import com.first_project.shopapp.models.ProductImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface IProductService {
    Product createProduct( ProductDTO productDTO) throws DataNotFoundException;
    Page<Product> getAllProducts(PageRequest pageRequest);
    Product getProductById(Long id) throws DataNotFoundException;
    Product updateProduct(Long id, ProductDTO productDTO) throws Exception;
    void deleteProduct(Long id);
    boolean existsByName(String nameProduct);
    ProductImage createProductImage(Long productId, ProductImageDTO productImageDTO) throws  Exception;
}
