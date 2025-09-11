package com.example.barkbuddy_backend.dto;

import java.time.LocalDateTime;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDTO {
    private Long id;
    private Long listingId;
    private String listingName;
    private String imageUrl;
    private Integer quantity;
    private String unitPrice;
    private String total;
    private String status;
    private LocalDateTime createdAt;
}
