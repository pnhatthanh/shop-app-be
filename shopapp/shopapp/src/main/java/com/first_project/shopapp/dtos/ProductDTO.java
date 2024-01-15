package com.first_project.shopapp.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    @NotBlank(message = "Name's product is required")
    private String name;
    @Min(value = 1, message = "Id's category must be >=1")
    private Long idCategory;
    @Min(value=0,message = "Price's product must be >=0")
    private float price;
    private String thumbnail;
    private String description;
}
