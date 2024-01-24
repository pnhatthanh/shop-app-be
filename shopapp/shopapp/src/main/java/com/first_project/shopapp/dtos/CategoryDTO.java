package com.first_project.shopapp.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {
    @NotBlank(message = "Category's name cannot be empty")
    private String name;
    private String symbol;
}
