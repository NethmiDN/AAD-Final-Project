package com.example.barkbuddy_backend.service.Impl;

import com.example.barkbuddy_backend.dto.DogDTO;
import com.example.barkbuddy_backend.entity.Dog;
import com.example.barkbuddy_backend.entity.Dog_Status;
import com.example.barkbuddy_backend.repo.DogRepository;
import com.example.barkbuddy_backend.service.DogService;
import com.example.barkbuddy_backend.util.ImgBBService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DogServiceImpl implements DogService {

    private final DogRepository dogRepository;
    private final ImgBBService imgBBService;

    private DogDTO toDTO(Dog dog) {
        return DogDTO.builder()
                .id(dog.getId())
                .dogName(dog.getDogName())
                .breed(dog.getBreed())
                .age(dog.getAge())
                .status(dog.getStatus().name())
                .ownerId(dog.getOwnerId())
                .imageUrl(dog.getImageUrl())
                .build();
    }

    private Dog toEntity(DogDTO dto, Long ownerId, String imageUrl) {
        return Dog.builder()
                .dogName(dto.getDogName())
                .breed(dto.getBreed())
                .age(dto.getAge())
                .status(Dog_Status.valueOf(dto.getStatus()))
                .ownerId(ownerId)
                .imageUrl(imageUrl)
                .build();
    }

    @Override
    public List<DogDTO> getAllDogs() {
        return dogRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public DogDTO createDog(DogDTO dogDTO, Long ownerId, byte[] imageBytes) {
        String uploadedUrl = imgBBService.uploadImage(imageBytes);
        Dog dog = toEntity(dogDTO, ownerId, uploadedUrl);
        Dog saved = dogRepository.save(dog);
        return toDTO(saved);
    }

    @Override
    public List<DogDTO> getDogsByOwnerId(Long ownerId) {
        return dogRepository.findByOwnerId(ownerId).stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public DogDTO updateDog(Long dogId, DogDTO dogDTO, Long ownerId, byte[] imageBytes) {
        Dog existingDog = dogRepository.findById(dogId)
                .orElseThrow(() -> new RuntimeException("Dog not found"));

        if (!existingDog.getOwnerId().equals(ownerId)) {
            throw new RuntimeException("Unauthorized");
        }

        String newImageUrl = existingDog.getImageUrl();
        if (imageBytes != null && imageBytes.length > 0) {
            newImageUrl = imgBBService.uploadImage(imageBytes);
        }

        existingDog.setDogName(dogDTO.getDogName());
        existingDog.setBreed(dogDTO.getBreed());
        existingDog.setAge(dogDTO.getAge());
        existingDog.setStatus(Dog_Status.valueOf(dogDTO.getStatus()));
        existingDog.setImageUrl(newImageUrl);

        Dog updated = dogRepository.save(existingDog);
        return toDTO(updated);
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
