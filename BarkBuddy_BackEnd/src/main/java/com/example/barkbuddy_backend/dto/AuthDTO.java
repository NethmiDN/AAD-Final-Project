package com.example.barkbuddy_backend.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthDTO {
    private String email;
    private String password;
}
