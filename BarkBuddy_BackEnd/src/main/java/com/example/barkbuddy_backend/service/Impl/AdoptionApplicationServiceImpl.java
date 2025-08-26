package com.example.barkbuddy_backend.service.Impl;

import com.example.barkbuddy_backend.entity.AdoptionApplications;
import com.example.barkbuddy_backend.entity.Adoption_Status;
import com.example.barkbuddy_backend.service.AdoptionApplicationService;
import com.example.barkbuddy_backend.repo.AdoptionApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
@RequiredArgsConstructor
public class AdoptionApplicationServiceImpl implements AdoptionApplicationService {
    private final AdoptionApplicationRepository adoptionRepo;

    @Override
    public AdoptionApplications requestAdoption(Long dogId, Long adopterId) {
        AdoptionApplications app = AdoptionApplications.builder()
                .dogId(dogId)
                .adopterId(adopterId)
                .status(Adoption_Status.PENDING)
                .timestamp(new Timestamp(System.currentTimeMillis()))
                .build();
        return adoptionRepo.save(app);
    }
}
