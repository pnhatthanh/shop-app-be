package com.first_project.shopapp.responses;

import com.first_project.shopapp.models.DetailOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DetailOrderResponse {
    private Long orderId;
    private Long productId;
    private float price;
    private int numberOfProducts;
    private float totalMoney;
    private String color;
    public static DetailOrderResponse fromDetailOrder(DetailOrder detailOrder){
        return DetailOrderResponse.builder()
                .orderId(detailOrder.getOrder().getId())
                .productId(detailOrder.getProduct().getId())
                .price(detailOrder.getPrice())
                .numberOfProducts(detailOrder.getNumberOfProduct())
                .totalMoney(detailOrder.getTotalMoney())
                .color(detailOrder.getColor())
                .build();
    }
}
