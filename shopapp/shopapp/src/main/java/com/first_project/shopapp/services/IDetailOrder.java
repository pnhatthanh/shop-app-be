package com.first_project.shopapp.services;

import com.first_project.shopapp.dtos.DetailOrderDTO;
import com.first_project.shopapp.exceptions.DataNotFoundException;
import com.first_project.shopapp.models.DetailOrder;

import java.util.List;

public interface IDetailOrder {
    DetailOrder createDetailOrder(DetailOrderDTO detailOrderDTO) throws DataNotFoundException;
    DetailOrder updateDetailOrder(Long id, DetailOrderDTO detailOrderDTO) throws DataNotFoundException;
    DetailOrder getDetailOrder(Long id);
    void deleteDetailOrder(Long id);
    List<DetailOrder> findByIdOrder(Long orderId);
}
