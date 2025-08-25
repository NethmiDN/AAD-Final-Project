package com.example.barkbuddy_backend.service;

import com.example.barkbuddy_backend.dto.DogDTO;

import java.util.List;

public interface DogService {
    DogDTO createDog(DogDTO dogDTO, Long ownerId);

    // Get all dogs (admin or general)
    List<DogDTO> getAllDogs();

    // Get dogs by ownerId (for My Dogs page)
    List<DogDTO> getDogsByOwnerId(Long ownerId);

    // Update dog by id (must match ownerId)
    DogDTO updateDog(Long dogId, DogDTO dogDTO, Long ownerId);

    // Delete dog by id (must match ownerId)
    void deleteDog(Long dogId, Long ownerId);

}