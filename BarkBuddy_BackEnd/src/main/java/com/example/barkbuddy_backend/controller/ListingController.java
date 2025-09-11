package com.example.barkbuddy_backend.controller;

import com.example.barkbuddy_backend.dto.ListingDTO;
import com.example.barkbuddy_backend.service.ListingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/listings")
@CrossOrigin(origins = "http://localhost:5500")
@RequiredArgsConstructor
public class ListingController {
    private final ListingService listingService;

    @GetMapping
    public ResponseEntity<List<ListingDTO>> getAllEquipments() {
        return ResponseEntity.ok(listingService.getAllEquipments());
    }

    @PostMapping
    public ResponseEntity<ListingDTO> addEquipment(@RequestBody ListingDTO dto) {
        return ResponseEntity.ok(listingService.addEquipment(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ListingDTO> updateEquipment(@PathVariable Long id, @RequestBody ListingDTO dto) {
        return ResponseEntity.ok(listingService.updateEquipment(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEquipment(@PathVariable Long id) {
        listingService.deleteEquipment(id);
        return ResponseEntity.ok().body("{\"message\":\"Deleted successfully\"}");
    }
}
