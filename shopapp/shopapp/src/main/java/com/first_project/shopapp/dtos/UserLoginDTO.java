package com.first_project.shopapp.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginDTO {
    @NotBlank(message = "Phone's number is required")
    private String phoneNumber;
    @NotBlank(message = "Password is required")
    private String password;
    private Long roleId;
}
