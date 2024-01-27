package com.first_project.shopapp.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse {
    private Long idProduct;
    private String nameProduct;
    private Long idCategory;
    private float price;
    private String description;
}
