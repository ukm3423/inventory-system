package com.masterservice.mapper;

import java.time.LocalDate;
import java.util.List;

import com.masterservice.dto.SaleDetailsDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SaleRequest {

    @NotBlank(message = "Customer name is required")
    private String customerName; 

    @NotNull(message = "Contact Number is required")
    private Long contactNo; 

    @NotNull(message = "Date is required")
    private LocalDate date; 

    @NotEmpty(message = "Add atleast one product")
    List<SaleDetailsDTO> saleDetailsList; 

}
