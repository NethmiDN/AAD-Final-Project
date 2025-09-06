package com.example.barkbuddy_backend.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SightingsDTO {
    private Long id;
    private String lostName;
    private String userName;
    private String sightingLocation;
    private String sightingDate;
}