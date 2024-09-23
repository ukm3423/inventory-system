package com.masterservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDTO {

    @NotNull(message="categoryId is required")
    private Long categoryId;

    @NotBlank(message="productCode is required")
    private String productCode; 

    @NotBlank(message="productName is required")
    private String productName; 
    
    @NotNull(message="price is required")
    private Double price;


}
