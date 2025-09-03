package com.example.barkbuddy_backend.dto;

import java.sql.Timestamp;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdoptionApplicationDTO {
    private Long id;
    private Long dogId;
    private Long adopterId;
    private String status;
    private Timestamp timestamp;
}
