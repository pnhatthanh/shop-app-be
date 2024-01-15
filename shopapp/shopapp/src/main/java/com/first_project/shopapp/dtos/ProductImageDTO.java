package com.first_project.shopapp.dtos;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductImageDTO {
    @Min(value=1, message = "Id's product must be >0")
    private Long productId;
    private String imageUrl;
}
