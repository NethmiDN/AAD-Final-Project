package com.example.barkbuddy_backend.controller;

import com.example.barkbuddy_backend.dto.AdoptionApplicationDTO;
import com.example.barkbuddy_backend.entity.AdoptionApplications;
import com.example.barkbuddy_backend.entity.Adoption_Status;
import com.example.barkbuddy_backend.entity.Dog;
import com.example.barkbuddy_backend.repo.AdoptionApplicationRepository;
import com.example.barkbuddy_backend.repo.DogRepository;
import com.example.barkbuddy_backend.service.AdoptionApplicationService;
import com.example.barkbuddy_backend.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/adoption")
@CrossOrigin(origins = "http://localhost:5500") // adjust to your frontend origin
@RequiredArgsConstructor
public class AdoptionApplicationController {

    private final AdoptionApplicationService adoptionService;
    private final AdoptionApplicationRepository adoptionRepo;
    private final DogRepository dogRepository;
    private final JWTUtil jwtUtil;

    @PostMapping("/request/{dogId}")
    public ResponseEntity<?> requestAdoption(@PathVariable Long dogId,
                                             @RequestBody Map<String,String> body,
                                             @RequestHeader("Authorization") String token) {
        Long adopterId = getUserIdFromToken(token);
        AdoptionApplications app = adoptionService.requestAdoption(dogId, adopterId);
        return ResponseEntity.ok(app);
    }

    @GetMapping("/all")
    public ResponseEntity<List<AdoptionApplicationDTO>> getAllAdoptions() {
        List<AdoptionApplications> apps = adoptionService.getAllRequests();
        List<AdoptionApplicationDTO> dtos = apps.stream()
                .map(app -> {
                    // Get dog name and adopter name using the service helper methods
                    String dogName = adoptionService.getDogNameById(app.getDogId());
                    String adopterName = adoptionService.getUserNameById(app.getAdopterId());

                    return AdoptionApplicationDTO.builder()
                            .id(app.getId())
                            .dogId(app.getDogId())
                            .dogName(dogName)
                            .adopterId(app.getAdopterId())
                            .adopterName(adopterName)
                            .status(app.getStatus().name())
                            .timestamp(app.getTimestamp())
                            .build();
                })
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/my-dogs")
    public ResponseEntity<?> getMyDogsAdoptionRequests(@RequestHeader("Authorization") String token) {
        try {
            Long ownerId = getUserIdFromToken(token);
            List<AdoptionApplicationDTO> requests = adoptionService.getRequestsForMyDogs(ownerId);
            return ResponseEntity.ok(requests);
        } catch (Exception e) {
            // Log the error for debugging
            e.printStackTrace();

            // Return a proper error message in the response
            Map<String, String> error = new HashMap<>();
            error.put("message", "Failed to fetch adoption requests: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    @GetMapping("/my-requests")
    public ResponseEntity<?> getMyAdoptionRequests(@RequestHeader("Authorization") String token) {
        try {
            Long adopterId = getUserIdFromToken(token);
            List<AdoptionApplications> requests = adoptionRepo.findByAdopterId(adopterId);

            List<Map<String, Object>> response = requests.stream().map(r -> {
                Map<String, Object> map = new HashMap<>();
                Dog dog = dogRepository.findById(r.getDogId()).orElse(null);
                map.put("dogName", dog != null ? dog.getDogName() : "Unknown");
                map.put("dogBreed", dog != null ? dog.getBreed() : "Unknown");
                map.put("status", r.getStatus());
                map.put("requestId", r.getId());
                map.put("timestamp", r.getTimestamp());
                return map;
            }).toList();

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error retrieving requests: " + e.getMessage());
        }
    }

    @PutMapping("/update-status/{id}")
    public ResponseEntity<?> updateAdoptionStatus(@PathVariable Long id,
                                                  @RequestParam String status,
                                                  @RequestHeader("Authorization") String token) {
        try {
            Long ownerId = getUserIdFromToken(token);
            AdoptionApplications app = adoptionRepo.findById(id)
                    .orElseThrow(() -> new RuntimeException("Request not found"));
            Dog dog = dogRepository.findById(app.getDogId())
                    .orElseThrow(() -> new RuntimeException("Dog not found"));

            if (!dog.getOwnerId().equals(ownerId)) {
                return ResponseEntity.status(403).body("Unauthorized");
            }

            app.setStatus(Adoption_Status.valueOf(status.toUpperCase()));
            adoptionRepo.save(app);
            return ResponseEntity.ok(app);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid status: " + status);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error updating status: " + e.getMessage());
        }
    }

    // Helper method to extract userId from JWT
    private Long getUserIdFromToken(String authHeader){
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
}
