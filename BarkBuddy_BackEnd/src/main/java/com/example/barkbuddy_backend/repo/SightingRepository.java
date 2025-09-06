package com.example.barkbuddy_backend.repo;

import com.example.barkbuddy_backend.entity.Sightings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SightingRepository extends JpaRepository<Sightings,Long> {
    List<Sightings> findByLostId(String lostId);
}
