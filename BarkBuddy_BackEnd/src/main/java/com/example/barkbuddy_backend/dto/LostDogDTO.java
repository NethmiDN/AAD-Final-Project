package com.example.barkbuddy_backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LostDogDTO {
    private Long id;
    private Long dogId;
    private Long userId;
    @NotBlank(message = "Last seen location is required")
    private String lastSeenLocation;
    private String description; // MISSING, FOUND
}
