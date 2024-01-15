package com.first_project.shopapp.services;

import com.first_project.shopapp.dtos.OrderDTO;
import com.first_project.shopapp.exceptions.DataNotFoundException;
import com.first_project.shopapp.exceptions.InvalidParamException;
import com.first_project.shopapp.models.Order;
import com.first_project.shopapp.models.OrderStatus;
import com.first_project.shopapp.models.User;
import com.first_project.shopapp.repositories.OrderRepository;
import com.first_project.shopapp.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
@Service
public class OrderService implements IOrderService{
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ModelMapper modelMapper;
    @Override
    public Order createOrder(OrderDTO orderDTO) throws Exception {
        User user=userRepository.findById(orderDTO.getUserId()).orElseThrow(
                ()->new DataNotFoundException("Cannot find user with id= "+orderDTO.getUserId())
        );
        modelMapper.typeMap(OrderDTO.class,Order.class)
                .addMappings(mapper->mapper.skip(Order::setId));
        Order order=new Order();
        modelMapper.map(orderDTO,order);
        order.setUser(user);
        order.setOrderDate(new Date());
        order.setStatus(OrderStatus.Pending);
        LocalDate shippingDate=orderDTO.getShippingDate()==null
                ?LocalDate.now():orderDTO.getShippingDate();
        if(shippingDate.isBefore(LocalDate.now())){
            throw new InvalidParamException("Shipping date must be at least today");
        }
        order.setShippingDate(shippingDate);
        order.setActive(true);
        return orderRepository.save(order);
    }

    @Override
    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteOrder(Long id) {
        //soft-delete
        Order order=orderRepository.findById(id).orElse(null);
        if(order!=null){
            order.setActive(false);
            orderRepository.save(order);
        }
    }

    @Override
    public Order updateOrder(Long id, OrderDTO orderDTO) throws DataNotFoundException {
        Order order = orderRepository.findById(id).orElseThrow(() ->
                new DataNotFoundException("Cannot find order with id: " + id));
        User existingUser = userRepository.findById(
                orderDTO.getUserId()).orElseThrow(() ->
                new DataNotFoundException("Cannot find user with id: " + id));
        modelMapper.typeMap(OrderDTO.class,Order.class)
                .addMappings(mapper->mapper.skip(Order::setId));
        modelMapper.map(orderDTO,order);
        order.setUser(existingUser);
        return orderRepository.save(order);
    }
    @Override
    public List<Order> findOrderByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }
}
