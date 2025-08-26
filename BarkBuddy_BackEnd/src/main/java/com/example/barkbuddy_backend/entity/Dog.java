package com.example.barkbuddy_backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "dogs")
public class Dog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String dogName;

    @Column(nullable = false)
    private String breed;

    @Column(nullable = false)
    private Integer age;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Dog_Status status; // AVAILABLE, ADOPTED, PENDING

    @Column(nullable = false)
    private Long ownerId;

    @Column(nullable = false)
    private String imageUrl;  // saved after uploading to ImgBB
}
