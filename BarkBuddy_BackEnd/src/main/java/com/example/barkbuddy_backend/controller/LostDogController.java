package com.example.barkbuddy_backend.controller;

import com.example.barkbuddy_backend.dto.LostDogDTO;
import com.example.barkbuddy_backend.entity.LostDog;
import com.example.barkbuddy_backend.entity.LostStatus;
import com.example.barkbuddy_backend.repo.LostDogRepository;
import com.example.barkbuddy_backend.service.LostDogService;
import com.example.barkbuddy_backend.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lostdog")
@CrossOrigin(origins = "http://localhost:5500")
@RequiredArgsConstructor
public class LostDogController {

    private final LostDogService lostDogService;
    private final LostDogRepository lostDogRepository;
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

    @PostMapping("/report")
    public ResponseEntity<LostDogDTO> reportMissing(@RequestParam Long dogId,
                                                    @RequestParam String lastSeenLocation,
                                                    @RequestHeader("Authorization") String authHeader) {
        Long userId = getUserIdFromToken(authHeader);
        LostDogDTO lostDogDTO = lostDogService.reportMissing(dogId, userId, lastSeenLocation);
        return ResponseEntity.ok(lostDogDTO);
    }

    @GetMapping("/missing")
    public List<LostDog> getMissingDogs() {
        return lostDogRepository.findByDescription(LostStatus.MISSING);
    }

}
