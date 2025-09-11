package com.example.barkbuddy_backend.service.Impl;

import com.example.barkbuddy_backend.dto.SightingsDTO;
import com.example.barkbuddy_backend.entity.Dog;
import com.example.barkbuddy_backend.entity.LostDog;
import com.example.barkbuddy_backend.entity.Sightings;
import com.example.barkbuddy_backend.entity.User;
import com.example.barkbuddy_backend.repo.DogRepository;
import com.example.barkbuddy_backend.repo.LostDogRepository;
import com.example.barkbuddy_backend.repo.SightingRepository;
import com.example.barkbuddy_backend.repo.UserRepository;
import com.example.barkbuddy_backend.service.EmailService;
import com.example.barkbuddy_backend.service.SightingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SightingServiceImpl implements SightingService {
    private final SightingRepository sightingRepository;
    private final UserRepository userRepository; // already exists in your project
    private final LostDogRepository lostDogRepository;
    private final DogRepository dogRepository;
    private final EmailService emailService;

    private SightingsDTO toDTO(Sightings s) {
        final String defaultName = "User " + s.getUserId();
        String resolvedName = defaultName;
        try {
            resolvedName = userRepository.findById(Long.valueOf(s.getUserId()))
                    .map(u -> {
                        String un = u.getUsername();
                        if (un != null && !un.isBlank()) return un;
                        String em = u.getEmail();
                        return (em != null && !em.isBlank()) ? em : defaultName;
                    })
                    .orElse(defaultName);
        } catch (NumberFormatException ignored) {
        }

        return SightingsDTO.builder()
                .id(s.getId())
                .userName(resolvedName)
                .sightingLocation(s.getSightingLocation())
                .sightingDate(s.getSightingDate())
                .build();
    }

    @Override
    public List<SightingsDTO> getSightingsForLostDog(Long lostDogId) {
        var list = sightingRepository.findByLostId(String.valueOf(lostDogId));
        return list.stream().map(this::toDTO).toList();
    }

    @Override
    public SightingsDTO createSighting(Long lostDogId, Long userId, String location) {
        String nowIso = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        Sightings s = Sightings.builder()
                .lostId(String.valueOf(lostDogId))
                .userId(String.valueOf(userId))
                .sightingLocation(location)
                .sightingDate(nowIso)
                .build();

    var saved = sightingRepository.save(s);

    // After saving, notify the dog's owner with the location via email
    try {
        LostDog lostDog = lostDogRepository.findById(lostDogId)
            .orElseThrow(() -> new RuntimeException("LostDog not found for id: " + lostDogId));
        Dog dog = dogRepository.findById(lostDog.getDogId())
            .orElseThrow(() -> new RuntimeException("Dog not found for id: " + lostDog.getDogId()));
        User owner = userRepository.findById(dog.getOwnerId())
            .orElseThrow(() -> new RuntimeException("Owner not found for id: " + dog.getOwnerId()));

        String subject = "Lost Dog Sighting: " + dog.getDogName();
        String body = "Hello " + owner.getUsername() + ",\n\n" +
            "Someone reported a sighting for your lost dog '" + dog.getDogName() + "'.\n" +
            "Reported location: " + location + "\n" +
            "Reported at: " + nowIso + "\n\n" +
            "Please log in to BarkBuddy to view all sightings.\n\n" +
            "Thanks,\nBarkBuddy Team";

        emailService.sendEmail(owner.getEmail(), subject, body);
    } catch (Exception e) {
        System.err.println("Failed to send lost sighting email: " + e.getMessage());
    }

    return toDTO(saved);
    }
}
