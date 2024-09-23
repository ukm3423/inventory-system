package com.masterservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.masterservice.models.Order;
import com.masterservice.models.OrderDetails;
import com.masterservice.models.Product;

public interface OrderDetailsRepository extends JpaRepository<OrderDetails, Long>{

    List<OrderDetails> findByOrder(Order order);

    List<OrderDetails> findByProductAndStatus(Product product, Integer status);


}
