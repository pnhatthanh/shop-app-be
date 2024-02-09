package com.first_project.shopapp.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "detail_order")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetailOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    private float price;
    @Column(name = "number_of_product")
    private int numberOfProduct;
    @Column(name = "total_money")
    private float totalMoney;
    private String color;
}
