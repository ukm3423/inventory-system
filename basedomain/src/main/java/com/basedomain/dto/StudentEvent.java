package com.basedomain.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentEvent {

    private Long id;
    private String fullname;
    private String email;
    private Long phoneNumber;
    private LocalDate dob;
    private String message;
    
}
