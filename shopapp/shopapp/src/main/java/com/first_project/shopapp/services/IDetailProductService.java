package com.first_project.shopapp.services;

import com.first_project.shopapp.exceptions.DataNotFoundException;
import com.first_project.shopapp.models.ProductImage;
import com.first_project.shopapp.responses.ProductImageResponse;
import com.first_project.shopapp.responses.ProductResponse;

import java.util.List;

public interface IDetailProductService {
     ProductResponse getDetailProduct(Long idProduct)throws DataNotFoundException;
     List<ProductImageResponse> getProductImage(Long idProduct) throws DataNotFoundException;
}
