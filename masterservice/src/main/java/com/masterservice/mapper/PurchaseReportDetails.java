package com.masterservice.mapper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PurchaseReportDetails {
    
    private String productName;
    private String categoryName;
    private Integer quantity;
    private Double rate;
    private Double totalAmount;
}
