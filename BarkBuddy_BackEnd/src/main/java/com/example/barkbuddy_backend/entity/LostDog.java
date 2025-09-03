package com.example.barkbuddy_backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "lostDog")
public class LostDog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long dogId;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String lastSeenLocation;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LostStatus description;
}
