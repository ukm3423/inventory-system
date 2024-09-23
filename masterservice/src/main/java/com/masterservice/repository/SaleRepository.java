package com.masterservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.masterservice.models.Sale;

public interface SaleRepository extends JpaRepository<Sale, Long> {

    Sale findByBillNumber(String billNumber);
    
}
