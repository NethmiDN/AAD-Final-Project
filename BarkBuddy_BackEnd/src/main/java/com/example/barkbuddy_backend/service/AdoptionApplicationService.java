package com.example.barkbuddy_backend.service;

import com.example.barkbuddy_backend.entity.AdoptionApplications;

public interface AdoptionApplicationService {
    AdoptionApplications requestAdoption(Long dogId, Long adopterId);

}
