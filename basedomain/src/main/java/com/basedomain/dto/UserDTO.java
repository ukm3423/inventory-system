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
public class UserDTO {

    private Long id;

    private String fullname;

    private String email;

    private String password;

    private LocalDate dob;

    private Long phoneNo;

    private Role role;

    private String message;

}
