package com.masterservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.masterservice.models.Category;
import com.masterservice.models.DeliveryDetails;

public interface DeliveryDetailsRepository extends JpaRepository<DeliveryDetails, Long>{

    List<DeliveryDetails> findByCategory(Category category);

    
}
