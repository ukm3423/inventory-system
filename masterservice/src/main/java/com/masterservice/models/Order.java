package com.masterservice.models;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="orders")
public class Order {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id; 

    @Column(name = "order_number", unique = true)
    private String orderNumber; 

    @Column(name = "invoice_number", unique = true, nullable=true)
    private String invoiceNumber;

    @Column(name="order_date")
    private LocalDate orderDate;

    @Column(name = "delivery_date", nullable= true)
    private LocalDate deliveryDate;

    @ManyToOne
    @JoinColumn(name="supplier_id", nullable=false)
    private Supplier supplier; 

    @Column(name="status" , columnDefinition = "integer default 0")
    private Integer status;

    @CreationTimestamp
    @Column(updatable=false)
    private LocalDateTime createdAt; 

    @UpdateTimestamp
    private LocalDateTime updatedAt;

}
