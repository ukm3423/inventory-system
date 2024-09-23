package com.masterservice.models;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="sales")
public class Sale {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id; 

    @Column(name = "customer_name")
	private String customerName; 
	
	@Column(name = "contact_no")
	private Long contactNo; 
	
	@Column(name = "bill_no", unique = true)
	private String billNumber;
	
	@Column(name = "bill_date")
	private LocalDate billDate;
    
	@Column(name = "sale_date")
	private LocalDate saleDate;

	@Column(name="status" , columnDefinition = "integer default 0")
    private Integer status;

	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_at", nullable = false)
	private LocalDateTime updatedAt;

}
