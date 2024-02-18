package com.first_project.shopapp.controllers;

import com.first_project.shopapp.dtos.OrderDTO;
import com.first_project.shopapp.models.Order;
import com.first_project.shopapp.responses.ResponseObject;
import com.first_project.shopapp.services.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/order")
public class OrderController {
    @Autowired
    OrderService orderService;
    @PostMapping("/create")
    public ResponseEntity<?> createOrder(@Valid @RequestBody OrderDTO orderDTO,
                                         BindingResult result){
        if(result.hasErrors()){
            List<String> messageErrors=result.getAllErrors().stream()
                    .map(error->error.getDefaultMessage()).toList();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid data provider");
        }
        try{
            orderService.createOrder(orderDTO);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok","","Create order successfully")
            );
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @GetMapping("/user/{user_id}")
    public ResponseEntity<?> getOrdersForUser(@Valid @PathVariable("user_id") Long user_id){
        try{
            List<Order> orders=orderService.findOrderByUserId(user_id);
            return ResponseEntity.status(HttpStatus.OK).body(orders);
        }catch (Exception e){
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable Long id){
        Order order=orderService.getOrderById(id);
        return ResponseEntity.status(HttpStatus.OK).body(order);
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrder(@Valid @PathVariable Long id
            , @RequestBody OrderDTO orderDTO, BindingResult result){
        if(result.hasErrors()){
            List<String> messageErrors=result.getAllErrors().stream()
                    .map(error->error.getDefaultMessage()).toList();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid data provider");
        }
        try{
            orderService.updateOrder(id,orderDTO);
            return ResponseEntity.status(HttpStatus.OK).body("Update successfully");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(e.getMessage());
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long id){
        orderService.deleteOrder(id);
        return ResponseEntity.status(HttpStatus.OK).body("Delete successfully");
    }
}
