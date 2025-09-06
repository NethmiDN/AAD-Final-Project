package com.example.barkbuddy_backend.service.Impl;

import com.example.barkbuddy_backend.dto.SightingsDTO;
import com.example.barkbuddy_backend.entity.Sightings;
import com.example.barkbuddy_backend.repo.SightingRepository;
import com.example.barkbuddy_backend.repo.UserRepository;
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

    private SightingsDTO toDTO(Sightings s) {
        String name = "User " + s.getUserId();
        try {
            Long uid = Long.parseLong(s.getUserId());
            var userOpt = userRepository.findById(uid);
            if (userOpt.isPresent()) {
                // try common name fields; adjust if your User class uses different field names
                var u = userOpt.get();
                try {
                    var fullNameField = u.getClass().getDeclaredField("fullName");
                    fullNameField.setAccessible(true);
                    Object v = fullNameField.get(u);
                    if (v != null) name = v.toString();
                } catch (Exception ignore) {
                    // fallback to email if available
                    try {
                        var emailField = u.getClass().getDeclaredField("email");
                        emailField.setAccessible(true);
                        Object v = emailField.get(u);
                        if (v != null) name = v.toString();
                    } catch (Exception ignored) {}
                }
            }
        } catch (NumberFormatException ignore) {
        }

        return SightingsDTO.builder()
                .id(s.getId())
                .userName(name)
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
        return toDTO(saved);
    }
}
