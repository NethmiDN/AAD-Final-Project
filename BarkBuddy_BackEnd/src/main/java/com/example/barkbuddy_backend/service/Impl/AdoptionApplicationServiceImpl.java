package com.example.barkbuddy_backend.service.Impl;

import com.example.barkbuddy_backend.dto.AdoptionApplicationDTO;
import com.example.barkbuddy_backend.entity.AdoptionApplications;
import com.example.barkbuddy_backend.entity.Adoption_Status;
import com.example.barkbuddy_backend.entity.Dog;
import com.example.barkbuddy_backend.entity.User;
import com.example.barkbuddy_backend.repo.DogRepository;
import com.example.barkbuddy_backend.repo.UserRepository;
import com.example.barkbuddy_backend.service.AdoptionApplicationService;
import com.example.barkbuddy_backend.service.EmailService;
import com.example.barkbuddy_backend.repo.AdoptionApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdoptionApplicationServiceImpl implements AdoptionApplicationService {
    private final AdoptionApplicationRepository adoptionRepo;
    private final DogRepository dogRepo;
    private final UserRepository userRepo;
        private final EmailService emailService;

    @Override
    public AdoptionApplications requestAdoption(Long dogId, Long adopterId) {
        AdoptionApplications app = AdoptionApplications.builder()
                .dogId(dogId)
                .adopterId(adopterId)
                .status(Adoption_Status.PENDING)
                .timestamp(new Timestamp(System.currentTimeMillis()))
                .build();
        AdoptionApplications saved = adoptionRepo.save(app);

        // After saving, send an email to the dog's owner
        Dog dog = dogRepo.findById(dogId)
                .orElseThrow(() -> new RuntimeException("Dog not found for ID: " + dogId));
        User owner = userRepo.findById(dog.getOwnerId())
                .orElseThrow(() -> new RuntimeException("Owner not found for ID: " + dog.getOwnerId()));
        User adopter = userRepo.findById(adopterId)
                .orElseThrow(() -> new RuntimeException("Adopter not found for ID: " + adopterId));

        String subject = "New Adoption Request for " + dog.getDogName();
        String body = "Hello " + owner.getUsername() + ",\n\n" +
                "You have a new adoption request for your dog '" + dog.getDogName() + "'.\n" +
                "Adopter: " + adopter.getUsername() + " (" + adopter.getEmail() + ")\n" +
                "Requested at: " + saved.getTimestamp() + "\n\n" +
                "Please log in to BarkBuddy to review and update the request status.\n\n" +
                "Thanks,\nBarkBuddy Team";

        try {
            emailService.sendEmail(owner.getEmail(), subject, body);
        } catch (Exception e) {
            // Log and continue; email failures shouldn't break the main flow
            System.err.println("Failed to send adoption email: " + e.getMessage());
        }

        return saved;
    }

    @Override
    public List<AdoptionApplications> getAllRequests() {
        return adoptionRepo.findAll();
    }

    // Get all adoption requests for dogs owned by the user
    @Override
    public List<AdoptionApplicationDTO> getRequestsForMyDogs(Long ownerId) {
        // First, find all dogs owned by this user
        List<Dog> myDogs = dogRepo.findByOwnerId(ownerId);

        // Get all adoption requests for these dogs
        List<AdoptionApplications> requestsForMyDogs = adoptionRepo.findAll()
                .stream()
                .filter(req -> myDogs.stream().anyMatch(dog -> dog.getId().equals(req.getDogId())))
                .toList();

        // Map to DTOs
        return requestsForMyDogs.stream()
                .map(req -> {
                    Dog dog = dogRepo.findById(req.getDogId()).orElseThrow(() ->
                            new RuntimeException("Dog not found for ID: " + req.getDogId())
                    );
                    User adopter = userRepo.findById(req.getAdopterId()).orElseThrow(() ->
                            new RuntimeException("User not found for ID: " + req.getAdopterId())
                    );
                    return AdoptionApplicationDTO.builder()
                            .id(req.getId())
                            .dogId(dog.getId())
                            .dogName(dog.getDogName())
                            .ownerUsername(adopter.getUsername())
                            .adopterName(adopter.getUsername())
                            .status(req.getStatus().name())
                            .timestamp(req.getTimestamp())
                            .build();
                }).toList();
    }

    // Update adoption request status
    @Override
    public void updateRequestStatus(Long requestId, String status) {
        AdoptionApplications app = adoptionRepo.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Adoption request not found"));
        app.setStatus(Adoption_Status.valueOf(status));
        adoptionRepo.save(app);
    }

    // Helper method to get dog name by dog ID
        @Override
        public String getDogNameById(Long dogId) {
        return dogRepo.findById(dogId)
                .map(Dog::getDogName)
                .orElse("Unknown Dog");
    }

    // Helper method to get user name by user ID
        @Override
        public String getUserNameById(Long userId) {
        return userRepo.findById(userId)
                .map(User::getUsername)
                .orElse("Unknown User");
    }
}
