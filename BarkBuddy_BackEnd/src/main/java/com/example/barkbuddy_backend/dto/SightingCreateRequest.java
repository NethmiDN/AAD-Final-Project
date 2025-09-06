package com.example.barkbuddy_backend.dto;

import lombok.Data;

@Data
public class SightingCreateRequest {
    private String location; // matches frontend body: { "location": "..." }
}
