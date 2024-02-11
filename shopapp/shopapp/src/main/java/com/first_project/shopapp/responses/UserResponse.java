package com.first_project.shopapp.responses;

import com.first_project.shopapp.models.User;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private Long id;
    private String fullName;
    private Date dateOfBirth;
    private String phoneNumber;
    private String address;
    private int facebookAccountId;
    private int googleAccountId;
    public static UserResponse fromUser(User user){
        return UserResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .dateOfBirth(user.getDateOfBirth())
                .phoneNumber(user.getPhoneNumber())
                .address(user.getAddress())
                .facebookAccountId(user.getGoogleAccountId())
                .googleAccountId(user.getGoogleAccountId())
                .build();
    }

}
