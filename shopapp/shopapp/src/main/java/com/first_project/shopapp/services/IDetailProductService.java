package com.first_project.shopapp.services;

import com.first_project.shopapp.exceptions.DataNotFoundException;
import com.first_project.shopapp.responses.ProductResponse;

import java.util.List;

public interface IDetailProductService {
     ProductResponse getDetailProduct(Long idProduct)throws DataNotFoundException;
}
