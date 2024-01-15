package com.first_project.shopapp.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product extends Base {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name_product",nullable = false)
    private String nameProduct;
    @ManyToOne
    @JoinColumn(name = "id_category")
    private Category category;
    private float price;
    private String thumbnail;
    private String description;
}
