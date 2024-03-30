package com.first_project.shopapp.repositories;

import com.first_project.shopapp.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
    @Query("SELECT o FROM Order o WHERE o.user.id = :userId AND o.active = true")
    List<Order> findByUserId(Long userId);
}
