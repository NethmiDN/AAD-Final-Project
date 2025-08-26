package com.example.barkbuddy_backend.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DogDTO {
    private Long id;
    private String dogName;
    private String breed;
    private Integer age;
    private String status;
    private Long ownerId;
    private String imageUrl;
}