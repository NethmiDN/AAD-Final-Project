package com.example.barkbuddy_backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "sightings")
public class Sightings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String lostId;
    @Column(nullable = false)
    private String userId;
    @Column(nullable = false)
    private String sightingLocation;
    @Column(nullable = false)
    private String sightingDate;
}
