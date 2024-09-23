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
public class SupplierDTO {

    @NotBlank
    private String supplierName;

    @NotBlank
    private String address;

    @NotBlank
    private String emailAddress;

    @NotNull
    private Long phone;

}
