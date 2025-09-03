package com.example.barkbuddy_backend.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LostDogDetailDTO {
    private Long id;
    private Long dogId;
    private Long userId;
    private String lastSeenLocation;
    private String status; // MISSING, FOUND
    
    // Dog details
    private String dogName;
    private String breed;
    private Integer age;
    private String imageUrl;
}
