package com.example.barkbuddy_backend.repo;

import com.example.barkbuddy_backend.entity.LostDog;
import com.example.barkbuddy_backend.entity.LostStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LostDogRepository extends JpaRepository<LostDog,Long> {
    LostDog save(LostDog lostDog);
    List<LostDog> findByDescription(LostStatus description);
}
