package com.first_project.shopapp.controllers;

import com.first_project.shopapp.dtos.DetailOrderDTO;
import com.first_project.shopapp.models.DetailOrder;
import com.first_project.shopapp.services.DetailOrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/detail_order")
public class OrderDetailController {
    @Autowired
    DetailOrderService detailOrderService;
    @PostMapping("/create")
    public ResponseEntity<?> createDetail(@Valid @RequestBody DetailOrderDTO detailOrderDTO,
                                          BindingResult result){
        if(result.hasErrors()){
            List<String> messageErrors=result.getAllErrors().stream()
                    .map(error->error.getDefaultMessage()).toList();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid data provider");
        }
        try{
            detailOrderService.createDetailOrder(detailOrderDTO);
            return ResponseEntity.status(HttpStatus.OK).body("Create detail order successfully");
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(e.getMessage());
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderDetail(@PathVariable Long id){
        try{
            DetailOrder detailOrder=detailOrderService.getDetailOrder(id);
            return ResponseEntity.status(HttpStatus.OK).body(detailOrder);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @GetMapping("/order/{order_id}")
    public ResponseEntity<?> getOrderDetails(@PathVariable Long order_id){
        try{
            List<DetailOrder> detailOrders=detailOrderService.findByIdOrder(order_id);
            return ResponseEntity.status(HttpStatus.OK).body(detailOrders);
        }catch(Exception e){
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateDetail(@Valid @RequestBody DetailOrderDTO detailOrderDTO,
                                          @PathVariable("id") Long id, BindingResult result){
        if(result.hasErrors()){
            List<String> messageErrors=result.getAllErrors().stream()
                    .map(error->error.getDefaultMessage()).toList();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageErrors);
        }
        try{
            detailOrderService.updateDetailOrder(id,detailOrderDTO);
            return ResponseEntity.status(HttpStatus.OK).body("Update successfully");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(e.getMessage());
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDetailOrder(@PathVariable Long id){
        detailOrderService.deleteDetailOrder(id);
        return ResponseEntity.status(HttpStatus.OK).body("Delete successfully");
    }
}
