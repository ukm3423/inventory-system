package com.masterservice.mapper;

import java.time.LocalDate;
import java.util.List;

import com.masterservice.dto.SaleDetailsDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SaleResponse {
    
    private String billNumber;
    private String customerName;
    private Long contactNo;
    private LocalDate saleDate;
    
    List<SaleDetailsDTO> saleDetailsList;

}
