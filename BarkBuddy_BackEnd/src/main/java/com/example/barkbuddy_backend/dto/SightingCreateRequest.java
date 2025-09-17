package com.example.barkbuddy_backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SightingCreateRequest {
    @NotBlank(message = "Sighting location is required")
    private String location; // matches frontend body: { "location": "..." }
}
