package com.masterservice.models;

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
@Table(name="products")
public class Product {
    

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id; 

    @ManyToOne
    @JoinColumn(name="category_id", nullable=false)
    private Category category;

    private String productCode; 

    private String productName; 
    
    private Double price;

    @Column(name="status" , columnDefinition = "integer default 0")
    private Integer status;

    @CreationTimestamp
    @Column(updatable=false)
    private LocalDateTime createdAt; 

    @UpdateTimestamp
    private LocalDateTime updatedAt;


}
