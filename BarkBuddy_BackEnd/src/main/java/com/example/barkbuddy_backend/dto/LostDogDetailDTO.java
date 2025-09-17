package com.example.barkbuddy_backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LostDogDetailDTO {
    private Long id;
    private Long dogId;
    private Long userId;
    @NotBlank(message = "Last seen location is required")
    private String lastSeenLocation;
    private String status; // MISSING, FOUND

    // Dog details
    @NotBlank(message = "Dog Name is required")
    private String dogName;
    @NotBlank(message = "Breed is required")
    private String breed;
    @NotBlank(message = "Age is required")
    private Integer age;
    @NotBlank(message = "Image is required")
    private String imageUrl;
}
