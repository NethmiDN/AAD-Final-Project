package com.example.barkbuddy_backend.service;

import com.example.barkbuddy_backend.dto.LostDogDTO;

public interface LostDogService {
    LostDogDTO reportMissing(Long dogId, Long userId, String lastSeenLocation);

}
