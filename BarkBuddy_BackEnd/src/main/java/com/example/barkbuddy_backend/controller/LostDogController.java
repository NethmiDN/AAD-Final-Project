package com.example.barkbuddy_backend.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.barkbuddy_backend.dto.LostDogDTO;
import com.example.barkbuddy_backend.dto.LostDogDetailDTO;
import com.example.barkbuddy_backend.dto.SightingCreateRequest;
import com.example.barkbuddy_backend.dto.SightingsDTO;
import com.example.barkbuddy_backend.entity.Dog;
import com.example.barkbuddy_backend.entity.LostDog;
import com.example.barkbuddy_backend.entity.LostStatus;
import com.example.barkbuddy_backend.repo.DogRepository;
import com.example.barkbuddy_backend.repo.LostDogRepository;
import com.example.barkbuddy_backend.service.LostDogService;
import com.example.barkbuddy_backend.service.SightingService;
import com.example.barkbuddy_backend.util.JWTUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/lostdog")
@CrossOrigin(origins = {"http://localhost:5500", "http://127.0.0.1:5500"})
@RequiredArgsConstructor
public class LostDogController {

    private final LostDogService lostDogService;
    private final LostDogRepository lostDogRepository;
    private final DogRepository dogRepository;
    private final JWTUtil jwtUtil;
    private final SightingService sightingService;

    private Long getUserIdFromToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new SecurityException("Invalid Authorization header");
        }
        String token = authHeader.substring(7);
        Long userId = jwtUtil.extractUserId(token);
        if (userId == null) {
            throw new SecurityException("Invalid or expired token");
        }
        return userId;
    }

    @PostMapping("/report")
    public ResponseEntity<LostDogDTO> reportMissing(@RequestParam Long dogId,
                                                    @RequestParam String lastSeenLocation,
                                                    @RequestHeader("Authorization") String authHeader) {
        Long userId = getUserIdFromToken(authHeader);
        LostDogDTO lostDogDTO = lostDogService.reportMissing(dogId, userId, lastSeenLocation);
        return ResponseEntity.ok(lostDogDTO);
    }

    @GetMapping("/missing")
    public List<LostDogDetailDTO> getMissingDogs() {
        List<LostDog> lostDogs = lostDogRepository.findByDescription(LostStatus.MISSING);
        List<LostDogDetailDTO> result = new ArrayList<>();

        for (LostDog lostDog : lostDogs) {
            Dog dog = dogRepository.findById(lostDog.getDogId()).orElse(null);
            if (dog != null) {
                LostDogDetailDTO detailDTO = LostDogDetailDTO.builder()
                        .id(lostDog.getId())
                        .dogId(lostDog.getDogId())
                        .userId(lostDog.getUserId())
                        .lastSeenLocation(lostDog.getLastSeenLocation())
                        .status(lostDog.getDescription().toString())
                        .dogName(dog.getDogName())
                        .breed(dog.getBreed())
                        .age(dog.getAge())
                        .imageUrl(dog.getImageUrl())
                        .build();
                result.add(detailDTO);
            }
        }

        return result;
    }

    @GetMapping("/{lostDogId}/sightings")
    public ResponseEntity<List<SightingsDTO>> getSightings(@PathVariable Long lostDogId) {
        return ResponseEntity.ok(sightingService.getSightingsForLostDog(lostDogId));
    }

    @PostMapping("/sighting/{lostDogId}")
    public ResponseEntity<SightingsDTO> createSighting(
            @PathVariable Long lostDogId,
            @RequestBody SightingCreateRequest request,
            @RequestHeader("Authorization") String authHeader
    ) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).build();
        }
        String token = authHeader.substring(7);
        Long userId = jwtUtil.extractUserId(token);
        if (userId == null) {
            return ResponseEntity.status(401).build();
        }

        var dto = sightingService.createSighting(lostDogId, userId, request.getLocation());
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{lostDogId}/found")
    public ResponseEntity<LostDogDTO> markFound(
            @PathVariable Long lostDogId,
            @RequestHeader("Authorization") String authHeader
    ) {
        try {
            Long userId = getUserIdFromToken(authHeader);
            LostDogDTO updated = lostDogService.markFound(lostDogId, userId);
            return ResponseEntity.ok(updated);
        } catch (SecurityException se) {
            return ResponseEntity.status(403).build();
        } catch (IllegalArgumentException iae) {
            return ResponseEntity.notFound().build();
        }
    }
}