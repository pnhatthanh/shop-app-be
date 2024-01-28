package com.first_project.shopapp.services;

import com.first_project.shopapp.exceptions.DataNotFoundException;
import com.first_project.shopapp.models.DetailProduct;
import com.first_project.shopapp.models.Product;
import com.first_project.shopapp.models.ProductImage;
import com.first_project.shopapp.repositories.DetailProductRepository;
import com.first_project.shopapp.repositories.ProductImageRepository;
import com.first_project.shopapp.repositories.ProductRepository;
import com.first_project.shopapp.responses.ProductImageResponse;
import com.first_project.shopapp.responses.ProductResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DetailProductService implements IDetailProductService{
    @Autowired
    DetailProductRepository detailProductRepository;
    @Autowired
    ProductImageRepository productImageRepository;
    @Autowired
    ProductRepository productRepository;
    @Override
    public ProductResponse getDetailProduct(Long idProduct) throws DataNotFoundException {
        Optional<DetailProduct> detailProduct=detailProductRepository.findByProductId(idProduct);
        if(detailProduct.isEmpty()){
            throw new DataNotFoundException("Product is not exists");
        }
        DetailProduct detail=detailProduct.get();
        return ProductResponse.builder()
                .idProduct(detail.getId())
                .idCategory(detail.getProduct().getCategory().getId())
                .nameProduct(detail.getProduct().getNameProduct())
                .price(detail.getProduct().getPrice())
                .description(detail.getDescription())
                .build();
    }

    @Override
    public List<ProductImageResponse> getProductImage(Long idProduct) throws DataNotFoundException {
        List<ProductImage> productImages=productImageRepository.findByProductId(idProduct);
        if(productImages.isEmpty()){
            throw new DataNotFoundException("Image's product is error");
        }
        return productImages.stream()
                .map(productImage -> new ProductImageResponse(productImage.getProduct().getId(),productImage.getImageUrl()))
                .collect(Collectors.toList());
    }
}
