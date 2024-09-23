package com.masterservice.mapper;

import java.time.LocalDate;
import java.util.List;

import com.masterservice.dto.OrderDetailsDTO;

import lombok.Data;

@Data
public class PlaceOrderRequest {
    
    private Long supplierId; 
    private LocalDate date; 

    List<OrderDetailsDTO> productDetailsList; 

}
