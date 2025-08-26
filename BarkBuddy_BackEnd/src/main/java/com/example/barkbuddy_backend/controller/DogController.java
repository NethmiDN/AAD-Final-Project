package com.example.barkbuddy_backend.controller;

import com.example.barkbuddy_backend.dto.DogDTO;
import com.example.barkbuddy_backend.service.DogService;
import com.example.barkbuddy_backend.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/dogs")
@CrossOrigin(origins = "http://localhost:5500")
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

    @PostMapping(
            value = "/saveDog",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<DogDTO> addDog(@RequestPart("dog") DogDTO dogDTO,
                                         @RequestPart("image") MultipartFile image,
                                         @RequestHeader("Authorization") String authHeader) throws Exception {
        Long ownerId = getUserIdFromToken(authHeader);
        byte[] imageBytes = image.getBytes();
        DogDTO createdDog = dogService.createDog(dogDTO, ownerId, imageBytes);
        return ResponseEntity.ok(createdDog);
    }

    @GetMapping
    public ResponseEntity<List<DogDTO>> getMyDogs(@RequestHeader("Authorization") String authHeader) {
        Long ownerId = getUserIdFromToken(authHeader);
        return ResponseEntity.ok(dogService.getDogsByOwnerId(ownerId));
    }

    @PutMapping(
            value = "/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<DogDTO> updateDog(@PathVariable Long id,
                                            @RequestPart("dog") DogDTO dogDTO,
                                            @RequestPart(value = "image", required = false) MultipartFile image,
                                            @RequestHeader("Authorization") String authHeader) throws Exception {
        Long ownerId = getUserIdFromToken(authHeader);
        byte[] imageBytes = image != null ? image.getBytes() : null;
        return ResponseEntity.ok(dogService.updateDog(id, dogDTO, ownerId, imageBytes));
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
        return ResponseEntity.ok(dogService.getAllDogs());
    }
}
