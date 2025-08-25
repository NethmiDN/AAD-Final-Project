package com.example.barkbuddy_backend.service;

import com.example.barkbuddy_backend.dto.DogDTO;

import java.util.List;

public interface DogService {
    DogDTO createDog(DogDTO dogDTO, Long ownerId);
    List<DogDTO> getAllDogs();
    List<DogDTO> getDogsByOwnerId(Long ownerId);
    DogDTO updateDog(Long dogId, DogDTO dogDTO, Long ownerId);
    void deleteDog(Long dogId, Long ownerId);
}
