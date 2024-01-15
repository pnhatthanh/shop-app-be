package com.first_project.shopapp.dtos;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetailOrderDTO {
    @Min(value=1,message = "Order's id must be >0")
    private Long orderId;
    @Min(value=1,message = "Product's id must be >0")
    private Long productId;
    @Min(value=0,message = "Price must be >0")
    private float price;
    @Min(value=1,message = "Number of product must be >0")
    private int numberOfProduct;
    @Min(value=0,message = "Total money must be >0")
    private float totalMoney;
    private String color;
}
