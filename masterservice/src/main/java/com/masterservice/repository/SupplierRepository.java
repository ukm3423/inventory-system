package com.masterservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.masterservice.models.Supplier;

public interface SupplierRepository extends JpaRepository<Supplier, Long>{

    public Supplier findByEmailAddress(String emailAddress);

    public Supplier findByPhone(Long phone);
}
