package com.first_project.shopapp.services;

import com.first_project.shopapp.dtos.ProductDTO;
import com.first_project.shopapp.dtos.ProductImageDTO;
import com.first_project.shopapp.exceptions.DataNotFoundException;
import com.first_project.shopapp.models.Base;
import com.first_project.shopapp.models.Category;
import com.first_project.shopapp.models.Product;
import com.first_project.shopapp.models.ProductImage;
import com.first_project.shopapp.repositories.CategoryRepository;
import com.first_project.shopapp.repositories.ProductImageRepository;
import com.first_project.shopapp.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class ProductService implements IProductService{
    @Autowired
    ProductRepository productRepository;
    @Autowired
    ProductImageRepository productImageRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Override
    public Product createProduct(ProductDTO productDTO) throws DataNotFoundException {
        Category category=categoryRepository.findById(productDTO.getIdCategory()).orElseThrow(
                ()-> new DataNotFoundException("Cannot find category with id= "+productDTO.getIdCategory())
        );
        Product product=new Product();
        product.setNameProduct(productDTO.getName());
        product.setCategory(category);
        product.setPrice(productDTO.getPrice());
        product.setDescription(productDTO.getDescription());
        product.setThumbnail(productDTO.getThumbnail());
        return productRepository.save(product);
    }

    @Override
    public Page<Product> getAllProducts(PageRequest pageRequest) {
        // Lấy danh sách sản phẩm theo trang(page) và giới hạn(limit)
        return productRepository.findAll(pageRequest);
    }

    @Override
    public Product getProductById(Long id) throws DataNotFoundException {
        return productRepository.findById(id).orElseThrow(
                ()->new DataNotFoundException("Cannot find product with id= "+id)
        );
    }

    @Override
    public Product updateProduct(Long id, ProductDTO productDTO) throws Exception {
        Product product=getProductById(id);
        Category category=categoryRepository.findById(productDTO.getIdCategory()).orElseThrow(
                ()-> new DataNotFoundException("Cannot find category with id= "+productDTO.getIdCategory())
        );
        product.setNameProduct(productDTO.getName());
        product.setCategory(category);
        product.setPrice(productDTO.getPrice());
        product.setDescription(productDTO.getDescription());
        product.setThumbnail(productDTO.getThumbnail());
        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public boolean existsByName(String nameProduct) {
        return productRepository.existsByNameProduct(nameProduct);
    }

    @Override
    public ProductImage createProductImage(Long productId, ProductImageDTO productImageDTO) throws Exception {
        Product product=productRepository.findById(productId).orElseThrow(
                ()-> new DataNotFoundException("Cannot find product with id= "+productId)
        );
        ProductImage productImage=new ProductImage();
        productImage.setProduct(product);
        productImage.setImageUrl(productImageDTO.getImageUrl());
        return productImageRepository.save(productImage);
    }
}
