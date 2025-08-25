package com.example.barkbuddy_backend.controller;

import com.example.barkbuddy_backend.dto.DogDTO;
import com.example.barkbuddy_backend.service.DogService;
import com.example.barkbuddy_backend.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dogs")
@RequiredArgsConstructor
public class DogController {

    private final DogService dogService;
    private final JWTUtil jwtUtil;

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

    //     @PreAuthorize("hasRole('ADMIN') or hasRole('USER')") // Temporarily commented out to fix 403
    @PostMapping("/saveDog")
    public ResponseEntity<DogDTO> addDog(@RequestBody DogDTO dogDTO,
                                         @RequestHeader("Authorization") String authHeader) {

        Long ownerId = getUserIdFromToken(authHeader);

        if (dogDTO.getDogName() == null || dogDTO.getBreed() == null
                || dogDTO.getAge() == null || dogDTO.getStatus() == null) {
            return ResponseEntity.badRequest().build();
        }

        DogDTO createdDog = dogService.createDog(dogDTO, ownerId);
        if (createdDog == null || createdDog.getId() == null) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok(createdDog);
    }

    @GetMapping
    public ResponseEntity<List<DogDTO>> getMyDogs(@RequestHeader("Authorization") String authHeader) {
        Long ownerId = getUserIdFromToken(authHeader);
        return ResponseEntity.ok(dogService.getDogsByOwnerId(ownerId));
    }

    @GetMapping("/all")
    public ResponseEntity<List<DogDTO>> getAllDogs() {
        return ResponseEntity.ok(dogService.getAllDogs());
    }

    @PutMapping("/{id}")
    public ResponseEntity<DogDTO> updateDog(@PathVariable Long id,
                                            @RequestBody DogDTO dogDTO,
                                            @RequestHeader("Authorization") String authHeader) {
        Long ownerId = getUserIdFromToken(authHeader);
        return ResponseEntity.ok(dogService.updateDog(id, dogDTO, ownerId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDog(@PathVariable Long id,
                                          @RequestHeader("Authorization") String authHeader) {
        Long ownerId = getUserIdFromToken(authHeader);
        dogService.deleteDog(id, ownerId);
        return ResponseEntity.noContent().build();
    }
}
