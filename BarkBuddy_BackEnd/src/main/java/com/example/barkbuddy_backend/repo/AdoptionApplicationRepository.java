package com.example.barkbuddy_backend.repo;

import com.example.barkbuddy_backend.entity.AdoptionApplications;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdoptionApplicationRepository extends JpaRepository<AdoptionApplications, Long> {
}
