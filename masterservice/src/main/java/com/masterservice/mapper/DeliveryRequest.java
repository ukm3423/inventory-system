package com.masterservice.mapper;

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
public class DeliveryRequest {

    private String supplierName; 
    private String orderDate; 
    private String orderCode;

    List<OrderDetailsDTO> orderDetailsList; 
}
