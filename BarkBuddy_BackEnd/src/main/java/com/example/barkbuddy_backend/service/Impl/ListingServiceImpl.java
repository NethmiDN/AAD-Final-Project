package com.example.barkbuddy_backend.service.Impl;

import com.example.barkbuddy_backend.dto.ListingDTO;
import com.example.barkbuddy_backend.entity.Listings;
import com.example.barkbuddy_backend.repo.ListingRepository;
import com.example.barkbuddy_backend.service.ListingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ListingServiceImpl implements ListingService {
    private final ListingRepository listingRepository;

    @Override
    public List<ListingDTO> getAllEquipments() {
        return listingRepository.findAll()
                .stream()
                .map(e -> ListingDTO.builder()
                        .id(e.getId())
                        .listingName(e.getListingName())
                        .listingDescription(e.getListingDescription())
                        .price(e.getPrice())
                        .quantity(e.getQuantity())
                        .imageUrl(e.getImageUrl())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public ListingDTO addEquipment(ListingDTO dto) {
        Listings equipment = Listings.builder()
                .listingName(dto.getListingName())
                .listingDescription(dto.getListingDescription())
                .price(dto.getPrice())
                .quantity(dto.getQuantity())
                .imageUrl(dto.getImageUrl())
                .build();
        listingRepository.save(equipment);
        dto.setId(equipment.getId());
        return dto;
    }

    @Override
    public ListingDTO updateEquipment(Long id, ListingDTO dto) {
        Listings equipment = listingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Equipment not found"));
        equipment.setListingName(dto.getListingName());
        equipment.setListingDescription(dto.getListingDescription());
        equipment.setPrice(dto.getPrice());
        equipment.setQuantity(dto.getQuantity());
        equipment.setImageUrl(dto.getImageUrl());
        listingRepository.save(equipment);
        dto.setId(equipment.getId());
        return dto;
    }

    @Override
    public void deleteEquipment(Long id) {
        if (!listingRepository.existsById(id)) {
            throw new RuntimeException("Equipment not found");
        }
        listingRepository.deleteById(id);
    }

}
