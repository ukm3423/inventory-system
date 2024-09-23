package com.masterservice.mapper;

import java.time.LocalDate;
import java.util.List;

import com.masterservice.dto.OrderDetailsDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse {
    
    private String orderNumber;
    private String supplierName;
    private LocalDate orderDate;
    
    List<OrderDetailsDTO> orderDetailsList;


}
