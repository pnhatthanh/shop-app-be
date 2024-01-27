package com.first_project.shopapp.services;

import com.first_project.shopapp.exceptions.DataNotFoundException;
import com.first_project.shopapp.models.DetailProduct;
import com.first_project.shopapp.models.Product;
import com.first_project.shopapp.repositories.DetailProductRepository;
import com.first_project.shopapp.repositories.ProductRepository;
import com.first_project.shopapp.responses.ProductResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DetailProductService implements IDetailProductService{
    @Autowired
    DetailProductRepository detailProductRepository;
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
}
