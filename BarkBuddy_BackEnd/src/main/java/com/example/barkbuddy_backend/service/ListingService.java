package com.example.barkbuddy_backend.service;

import com.example.barkbuddy_backend.dto.ListingDTO;

import java.util.List;

public interface ListingService {
    List<ListingDTO> getAllEquipments();
    ListingDTO addEquipment(ListingDTO listingDTO);
    ListingDTO updateEquipment(Long id, ListingDTO listingDTO);
    void deleteEquipment(Long id);
    ListingDTO purchaseEquipment(Long id, int quantity);
}
