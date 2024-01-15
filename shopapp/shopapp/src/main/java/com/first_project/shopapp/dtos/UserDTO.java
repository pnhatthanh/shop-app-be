package com.first_project.shopapp.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String fullName;
    private Date dateOfBirth;
    @NotBlank(message = "Phone's number is required")
    private String phoneNumber;
    private String address;
    @NotBlank(message = "Password cannot be empty")
    private String password;
    private String retypePassword;
    private int facebookAccountId;
    private int googleAccountId;
    @NotNull(message = "Role cannot be empty")
    private Long roleId;
}
