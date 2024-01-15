package com.first_project.shopapp.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "social_account")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SocialAccounnt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String provider;
    @Column(name = "provider_id")
    private String providerId;
    private String email;
    private String name;
}
