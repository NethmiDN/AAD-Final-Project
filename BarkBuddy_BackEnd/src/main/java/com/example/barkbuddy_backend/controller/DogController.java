package com.example.barkbuddy_backend.controller;

import com.example.barkbuddy_backend.dto.DogDTO;
import com.example.barkbuddy_backend.service.DogService;
import com.example.barkbuddy_backend.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dogs")
@CrossOrigin(origins = "http://localhost:5500") // frontend URL
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

    @PostMapping("/saveDog")
    public ResponseEntity<DogDTO> addDog(@RequestBody DogDTO dogDTO,
                                         @RequestHeader("Authorization") String authHeader) {
        Long ownerId = getUserIdFromToken(authHeader);
        DogDTO createdDog = dogService.createDog(dogDTO, ownerId);
        return ResponseEntity.ok(createdDog);
    }

    @GetMapping
    public ResponseEntity<List<DogDTO>> getMyDogs(@RequestHeader("Authorization") String authHeader) {
        Long ownerId = getUserIdFromToken(authHeader);
        List<DogDTO> dogs = dogService.getDogsByOwnerId(ownerId);
        return ResponseEntity.ok(dogs);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DogDTO> updateDog(@PathVariable Long id,
                                            @RequestBody DogDTO dogDTO,
                                            @RequestHeader("Authorization") String authHeader) {
        Long ownerId = getUserIdFromToken(authHeader);
        DogDTO updatedDog = dogService.updateDog(id, dogDTO, ownerId);
        return ResponseEntity.ok(updatedDog);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDog(@PathVariable Long id,
                                          @RequestHeader("Authorization") String authHeader) {
        Long ownerId = getUserIdFromToken(authHeader);
        dogService.deleteDog(id, ownerId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<DogDTO>> getAllDogs() {
        List<DogDTO> dogs = dogService.getAllDogs();
        return ResponseEntity.ok(dogs);
    }

}
