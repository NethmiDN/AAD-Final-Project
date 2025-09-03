package com.example.barkbuddy_backend.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LostDogDTO {
    private Long id;
    private Long dogId;
    private Long userId;
    private String lastSeenLocation;
    private String description; // MISSING, FOUND
}
