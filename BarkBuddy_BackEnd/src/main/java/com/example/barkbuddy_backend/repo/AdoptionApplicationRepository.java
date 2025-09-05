package com.example.barkbuddy_backend.repo;

import com.example.barkbuddy_backend.entity.AdoptionApplications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdoptionApplicationRepository extends JpaRepository<AdoptionApplications, Long> {
    List<AdoptionApplications> findByDogId(Long dogId);
    List<AdoptionApplications> findByAdopterId(Long adopterId);
    Optional<AdoptionApplications> findByAdopterIdAndDogId(Long adopterId, Long dogId);
}
