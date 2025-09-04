package com.example.barkbuddy_backend.service;

import com.example.barkbuddy_backend.dto.AdoptionApplicationDTO;
import com.example.barkbuddy_backend.entity.AdoptionApplications;

import java.util.List;

public interface AdoptionApplicationService {
    AdoptionApplications requestAdoption(Long dogId, Long adopterId);
    List<AdoptionApplications> getAllRequests();
    List<AdoptionApplicationDTO> getRequestsForMyDogs(Long ownerId);
    void updateRequestStatus(Long requestId, String status);

    // Helper methods to get names by ID
    String getDogNameById(Long dogId);
    String getUserNameById(Long userId);

}
