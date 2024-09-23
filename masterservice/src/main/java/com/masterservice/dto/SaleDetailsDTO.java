package com.masterservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SaleDetailsDTO {
    
    private Long categoryId;
    private Long productId;
    private Integer quantity;
    private Double rate;
    private String productName;
    private String categoryName;


}
