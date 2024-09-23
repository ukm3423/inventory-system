package com.masterservice.mapper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CurrentStockFormatter {

    private String categoryName;
    private String productName; 
	private Integer purchaseQty;
	private Integer saleQty;
	private Integer availableQty;

	
}
