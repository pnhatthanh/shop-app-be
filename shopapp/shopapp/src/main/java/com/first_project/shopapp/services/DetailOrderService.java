package com.first_project.shopapp.services;

import com.first_project.shopapp.dtos.DetailOrderDTO;
import com.first_project.shopapp.exceptions.DataNotFoundException;
import com.first_project.shopapp.models.DetailOrder;
import com.first_project.shopapp.models.Order;
import com.first_project.shopapp.models.Product;
import com.first_project.shopapp.repositories.DetailOrderRepository;
import com.first_project.shopapp.repositories.OrderRepository;
import com.first_project.shopapp.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class DetailOrderService implements IDetailOrder {
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    DetailOrderRepository detailOrderRepository;
    @Autowired
    ProductRepository productRepository;
    @Override
    public DetailOrder createDetailOrder(DetailOrderDTO detailOrderDTO) throws DataNotFoundException {
        Order order =orderRepository.findById(detailOrderDTO.getOrderId()).orElseThrow(
                ()-> new DataNotFoundException("Cannot find order with id= "+detailOrderDTO.getOrderId())
        );
        Product product=productRepository.findById(detailOrderDTO.getProductId()).orElseThrow(
                ()->new RuntimeException("Cannot find product with id="+detailOrderDTO.getProductId())
        );
        DetailOrder detailOrder=new DetailOrder();
        detailOrder.setOrder(order);
        detailOrder.setProduct(product);
        detailOrder.setNumberOfProduct(detailOrderDTO.getNumberOfProduct());
        detailOrder.setPrice(detailOrderDTO.getPrice());
        detailOrder.setColor(detailOrderDTO.getColor());
        detailOrder.setTotalMoney(detailOrderDTO.getTotalMoney());
        return detailOrderRepository.save(detailOrder);
    }

    @Override
    public DetailOrder updateDetailOrder(Long id, DetailOrderDTO detailOrderDTO) throws DataNotFoundException {
        DetailOrder detailOrder=detailOrderRepository.findById(id).orElseThrow(
                ()->new DataNotFoundException("Cannot find detail order with id= "+id)
        );
        Order order =orderRepository.findById(detailOrderDTO.getOrderId()).orElseThrow(
                ()-> new DataNotFoundException("Cannot find order with id= "+detailOrderDTO.getOrderId())
        );
        Product product=productRepository.findById(detailOrderDTO.getProductId()).orElseThrow(
                ()->new DataNotFoundException("Cannot find product with id="+detailOrderDTO.getProductId())
        );
        detailOrder.setOrder(order);
        detailOrder.setProduct(product);
        detailOrder.setNumberOfProduct(detailOrderDTO.getNumberOfProduct());
        detailOrder.setPrice(detailOrderDTO.getPrice());
        detailOrder.setColor(detailOrderDTO.getColor());
        detailOrder.setTotalMoney(detailOrderDTO.getTotalMoney());
        return detailOrderRepository.save(detailOrder);
    }

    @Override
    public DetailOrder getDetailOrder(Long id) {
        return detailOrderRepository.findById(id).orElseGet(null);
    }

    @Override
    public void deleteDetailOrder(Long id) {
        detailOrderRepository.deleteById(id);
    }

    @Override
    public List<DetailOrder> findByIdOrder(Long orderId) {
        return detailOrderRepository.findByOrderId(orderId);

    }
}
