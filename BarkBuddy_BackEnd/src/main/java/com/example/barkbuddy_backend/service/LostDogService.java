package com.example.barkbuddy_backend.service;

import com.example.barkbuddy_backend.dto.LostDogDTO;

public interface LostDogService {
    LostDogDTO reportMissing(Long dogId, Long userId, String lastSeenLocation);

    /**
     * Mark a reported lost dog as FOUND. Only the owner who reported the dog missing can do this.
     * @param lostDogId the LostDog record id
     * @param requesterUserId the id of the user performing the action (from JWT)
     * @return updated LostDogDTO with FOUND status
     * @throws SecurityException if the requester is not the owner
     * @throws IllegalArgumentException if the record cannot be found
     */
    LostDogDTO markFound(Long lostDogId, Long requesterUserId);

}
