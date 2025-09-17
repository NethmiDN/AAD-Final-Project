package com.example.barkbuddy_backend.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DogDTO {
    private Long id;
    @NotBlank(message = "Dog Name is required")
    private String dogName;
    @NotBlank(message = "Breed is required")
    private String breed;
    @NotBlank(message = "Age is required")
    private Integer age;
    private String status;
    private Long ownerId;
    @NotBlank(message = "Image is required")
    private String imageUrl;
}