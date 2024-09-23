package com.masterservice.mapper;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StockRequest {
    
    private Long productId;
    private Long categoryId;
}
