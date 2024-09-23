package com.masterservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.masterservice.models.Delivery;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    Delivery findByInvoiceNumber(String invoiceNumber);
    
}
