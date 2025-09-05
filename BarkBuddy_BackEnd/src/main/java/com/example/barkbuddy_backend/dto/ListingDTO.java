package com.example.barkbuddy_backend.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ListingDTO {
    private Long id;
    private String listingName;
    private String listingDescription;
    private String price;
    private Integer quantity;
    private String imageUrl;
}
