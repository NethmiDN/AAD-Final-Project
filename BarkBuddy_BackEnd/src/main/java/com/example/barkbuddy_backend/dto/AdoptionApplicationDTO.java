package com.example.barkbuddy_backend.dto;

import java.sql.Timestamp;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdoptionApplicationDTO {
    private Long id;
    private Long dogId;
    private String dogName;  // Added dog name field
    private Long adopterId;
    private String adopterName;  // Added adopter name field
    private String status;
    private Timestamp timestamp;
}
