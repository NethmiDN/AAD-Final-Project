package com.example.barkbuddy_backend.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/sightings")
@CrossOrigin(origins = "http://localhost:5500")
@RequiredArgsConstructor
public class SightingController {
//    private final SightingService sightingService;
//
//    /**
//     * Report a new sighting for a lost dog
//     */
//    @PostMapping("/{lostDogId}")
//    public ResponseEntity<SightingsDTO> reportSighting(
//            @PathVariable Long lostDogId,
//            @RequestBody SightingsDTO sightingDTO,
//            Authentication authentication
//    ) {
//        // Current user is taken from JWT
//        Long userId = Long.parseLong(authentication.getName());
//
//        SightingsDTO saved = sightingService.reportSighting(lostDogId, userId, sightingDTO.getLocation());
//        return ResponseEntity.ok(saved);
//    }
//
//    /**
//     * Get all sightings for a lost dog
//     */
//    @GetMapping("/dog/{lostDogId}")
//    public ResponseEntity<List<SightingsDTO>> getSightingsByLostDog(
//            @PathVariable Long lostDogId
//    ) {
//        return ResponseEntity.ok(sightingService.getSightingsByLostDog(lostDogId));
//    }
//
//    /**
//     * Get sightings reported by the logged-in user
//     */
//    @GetMapping("/me")
//    public ResponseEntity<List<SightingsDTO>> getMySightings(Authentication authentication) {
//        Long userId = Long.parseLong(authentication.getName());
//        return ResponseEntity.ok(sightingService.getSightingsByUser(userId));
//    }
//
//    /**
//     * Delete a sighting (only reporter or admin)
//     */
//    @DeleteMapping("/{sightingId}")
//    public ResponseEntity<Void> deleteSighting(
//            @PathVariable Long sightingId,
//            Authentication authentication
//    ) {
//        Long userId = Long.parseLong(authentication.getName());
//        sightingService.deleteSighting(sightingId, userId);
//        return ResponseEntity.noContent().build();
//    }
}