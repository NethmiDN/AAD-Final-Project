package com.example.barkbuddy_backend.service.Impl;

import org.springframework.stereotype.Service;

import com.example.barkbuddy_backend.dto.LostDogDTO;
import com.example.barkbuddy_backend.entity.LostDog;
import com.example.barkbuddy_backend.entity.LostStatus;
import com.example.barkbuddy_backend.repo.LostDogRepository;
import com.example.barkbuddy_backend.service.LostDogService;

import lombok.RequiredArgsConstructor;

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

    @Override
    public LostDogDTO markFound(Long lostDogId, Long requesterUserId) {
        LostDog lostDog = lostDogRepo.findById(lostDogId)
                .orElseThrow(() -> new IllegalArgumentException("Lost dog not found"));

        if (!lostDog.getUserId().equals(requesterUserId)) {
            throw new SecurityException("Not authorized to update this record");
        }

        lostDog.setDescription(LostStatus.FOUND);
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
