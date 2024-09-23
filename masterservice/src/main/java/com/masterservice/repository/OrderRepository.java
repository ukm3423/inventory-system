package com.masterservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.masterservice.models.Order;

public interface OrderRepository extends JpaRepository<Order, Long>{

    Order findByOrderNumber(String orderNumber);
}
