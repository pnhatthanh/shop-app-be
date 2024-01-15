package com.first_project.shopapp.repositories;

import com.first_project.shopapp.models.DetailOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DetailOrderRepository extends JpaRepository<DetailOrder,Long> {
    List<DetailOrder> findByOrderId(Long orderId);
}
