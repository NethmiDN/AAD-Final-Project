package com.example.barkbuddy_backend.service;

import com.example.barkbuddy_backend.dto.SightingsDTO;

import java.util.List;

public interface SightingService {
    List<SightingsDTO> getSightingsForLostDog(Long lostDogId);
    SightingsDTO createSighting(Long lostDogId, Long userId, String location);
}
