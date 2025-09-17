package com.example.barkbuddy_backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SightingsDTO {
    private Long id;
    private String lostName;
    private String userName;
    @NotBlank(message = "Sighting location is required")
    private String sightingLocation;
    private String sightingDate;
}