package com.example.barkbuddy_backend.service.Impl;

import com.example.barkbuddy_backend.dto.LostDogDTO;
import com.example.barkbuddy_backend.entity.LostDog;
import com.example.barkbuddy_backend.entity.LostStatus;
import com.example.barkbuddy_backend.repo.LostDogRepository;
import com.example.barkbuddy_backend.service.LostDogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LostDogServiceImpl implements LostDogService {

    private final LostDogRepository lostDogRepo;

    @Override
    public LostDogDTO reportMissing(Long dogId, Long userId, String lastSeenLocation) {
        LostDog lostDog = LostDog.builder()
                .dogId(dogId)
                .userId(userId)
                .lastSeenLocation(lastSeenLocation)
                .description(LostStatus.MISSING)
                .build();
        LostDog saved = lostDogRepo.save(lostDog);

        return LostDogDTO.builder()
                .id(saved.getId())
                .dogId(saved.getDogId())
                .userId(saved.getUserId())
                .lastSeenLocation(saved.getLastSeenLocation())
                .description(saved.getDescription().name())
                .build();
    }
}
