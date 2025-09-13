package com.example.barkbuddy_backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long paymentId; // Payment ID (auto-generated)

    @Column(name = "listing_name", nullable = false)
    private String listingName;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    // Keep as String for consistency with existing price representation
    @Column(name = "price", nullable = false)
    private String price;
}
