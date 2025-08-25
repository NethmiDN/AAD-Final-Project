package com.example.barkbuddy_backend.service.Impl;


import com.example.barkbuddy_backend.dto.DogDTO;
import com.example.barkbuddy_backend.entity.Dog;
import com.example.barkbuddy_backend.entity.Dog_Status;
import com.example.barkbuddy_backend.repo.DogRepository;
import com.example.barkbuddy_backend.service.DogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class DogServiceImpl implements DogService {
    private final DogRepository dogRepository;
    private static final Logger logger = LoggerFactory.getLogger(DogServiceImpl.class);

    private DogDTO toDTO(Dog dog) {
        return DogDTO.builder()
                .id(dog.getId())
                .dogName(dog.getDogName())
                .breed(dog.getBreed())
                .age(dog.getAge())
                .status(dog.getStatus().name())
                .ownerId(dog.getOwnerId())
                .build();
    }

    private Dog toEntity(DogDTO dto, Long ownerId) {
        return Dog.builder()
                .dogName(dto.getDogName())
                .breed(dto.getBreed())
                .age(dto.getAge())
                .status(Dog_Status.valueOf(dto.getStatus()))
                .ownerId(ownerId)
                .build();
    }

    @Override
    public DogDTO createDog(DogDTO dogDTO, Long ownerId) {
        try {
            logger.info("Creating dog: {}", dogDTO);
            Dog dog = toEntity(dogDTO, ownerId);
            Dog savedDog = dogRepository.save(dog);
            logger.info("Dog created successfully: {}", savedDog);
            return toDTO(savedDog);
        } catch (Exception e) {
            logger.error("Error creating dog: {}", e.getMessage());
            throw new RuntimeException("Failed to create dog: " + e.getMessage());
        }
    }

    @Override
    public List<DogDTO> getAllDogs() {
        try {
            List<Dog> dogs = dogRepository.findAll();
            return dogs.stream().map(this::toDTO).collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error getting all dogs: {}", e.getMessage());
            throw new RuntimeException("Failed to get dogs: " + e.getMessage());
        }
    }

    @Override
    public List<DogDTO> getDogsByOwnerId(Long ownerId) {
        try {
            List<Dog> dogs = dogRepository.findByOwnerId(ownerId);
            return dogs.stream().map(this::toDTO).collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error getting dogs for owner {}: {}", ownerId, e.getMessage());
            throw new RuntimeException("Failed to get dogs for owner: " + e.getMessage());
        }
    }

    @Override
    public DogDTO updateDog(Long dogId, DogDTO dogDTO, Long ownerId) {
        try {
            Dog existingDog = dogRepository.findById(dogId)
                    .orElseThrow(() -> new RuntimeException("Dog not found"));

            if (!existingDog.getOwnerId().equals(ownerId)) {
                throw new RuntimeException("Unauthorized to update this dog");
            }

            existingDog.setDogName(dogDTO.getDogName());
            existingDog.setBreed(dogDTO.getBreed());
            existingDog.setAge(dogDTO.getAge());
            existingDog.setStatus(Dog_Status.valueOf(dogDTO.getStatus()));

            Dog updatedDog = dogRepository.save(existingDog);
            return toDTO(updatedDog);
        } catch (Exception e) {
            logger.error("Error updating dog {}: {}", dogId, e.getMessage());
            throw new RuntimeException("Failed to update dog: " + e.getMessage());
        }
    }

    @Override
    public void deleteDog(Long dogId, Long ownerId) {
        try {
            Dog dog = dogRepository.findById(dogId)
                    .orElseThrow(() -> new RuntimeException("Dog not found"));

            if (!dog.getOwnerId().equals(ownerId)) {
                throw new RuntimeException("Unauthorized to delete this dog");
            }

            dogRepository.deleteById(dogId);
        } catch (Exception e) {
            logger.error("Error deleting dog {}: {}", dogId, e.getMessage());
            throw new RuntimeException("Failed to delete dog: " + e.getMessage());
        }
    }
}
