package com.example.barkbuddy_backend.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDTO {
    private String token;
    private String message;
    private Long userId;
    private String email;
}
