package com.example.barkbuddy_backend.repo;

import com.example.barkbuddy_backend.entity.Dog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DogRepository extends JpaRepository<Dog,Long> {
    List<Dog> findByOwnerId(Long ownerId);
    List<Dog> findByStatus(String status);
}
