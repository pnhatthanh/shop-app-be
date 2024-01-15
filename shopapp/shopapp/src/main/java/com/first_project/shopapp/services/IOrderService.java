package com.first_project.shopapp.services;

import com.first_project.shopapp.dtos.OrderDTO;
import com.first_project.shopapp.exceptions.DataNotFoundException;
import com.first_project.shopapp.models.Order;

import java.util.List;

public interface IOrderService {
    Order createOrder(OrderDTO orderDTO) throws Exception;
    Order getOrderById(Long id);
    void deleteOrder(Long id);
    Order updateOrder(Long id, OrderDTO orderDTO) throws DataNotFoundException;
    List<Order> findOrderByUserId(Long userId);
}
