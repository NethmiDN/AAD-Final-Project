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

@Service
@RequiredArgsConstructor
public class DogServiceImpl implements DogService {

    private final DogRepository dogRepository;

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

    @Override
    public List<DogDTO> getAllDogs() {
        List<Dog> dogs = dogRepository.findAll();
        return dogs.stream().map(this::toDTO).collect(Collectors.toList());
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
        Dog dog = toEntity(dogDTO, ownerId);
        Dog savedDog = dogRepository.save(dog);
        return toDTO(savedDog);
    }

    @Override
    public List<DogDTO> getDogsByOwnerId(Long ownerId) {
        List<Dog> dogs = dogRepository.findByOwnerId(ownerId);
        return dogs.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public DogDTO updateDog(Long dogId, DogDTO dogDTO, Long ownerId) {
        Dog existingDog = dogRepository.findById(dogId)
                .orElseThrow(() -> new RuntimeException("Dog not found"));

        if (!existingDog.getOwnerId().equals(ownerId)) {
            throw new RuntimeException("Unauthorized");
        }

        existingDog.setDogName(dogDTO.getDogName());
        existingDog.setBreed(dogDTO.getBreed());
        existingDog.setAge(dogDTO.getAge());
        existingDog.setStatus(Dog_Status.valueOf(dogDTO.getStatus()));

        Dog updatedDog = dogRepository.save(existingDog);
        return toDTO(updatedDog);
    }

    @Override
    public void deleteDog(Long dogId, Long ownerId) {
        Dog existingDog = dogRepository.findById(dogId)
                .orElseThrow(() -> new RuntimeException("Dog not found"));

        if (!existingDog.getOwnerId().equals(ownerId)) {
            throw new RuntimeException("Unauthorized");
        }

        dogRepository.deleteById(dogId);
    }
}
