package com.example.barkbuddy_backend.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.barkbuddy_backend.entity.LostDog;
import com.example.barkbuddy_backend.entity.LostStatus;

@Repository
public interface LostDogRepository extends JpaRepository<LostDog,Long> {
    LostDog save(LostDog lostDog);
    List<LostDog> findByDescription(LostStatus description);
}