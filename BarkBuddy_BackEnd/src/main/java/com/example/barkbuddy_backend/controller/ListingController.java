package com.example.barkbuddy_backend.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.barkbuddy_backend.dto.ListingDTO;
import com.example.barkbuddy_backend.service.ListingService;
import com.example.barkbuddy_backend.util.ImgBBService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/listings")
@CrossOrigin(origins = "http://localhost:5500")
@RequiredArgsConstructor
public class ListingController {
    private final ListingService listingService;
    private final ImgBBService imgBBService;

    @GetMapping
    public ResponseEntity<List<ListingDTO>> getAllEquipments() {
        return ResponseEntity.ok(listingService.getAllEquipments());
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ListingDTO> addEquipment(@RequestParam("listingName") String listingName,
                                                   @RequestParam("listingDescription") String listingDescription,
                                                   @RequestParam("price") String price,
                                                   @RequestParam("quantity") Integer quantity,
                                                   @RequestPart(value = "image", required = false) MultipartFile image) {
        String imageUrl = null;
        try {
            if (image != null && !image.isEmpty()) {
                imageUrl = imgBBService.uploadImage(image.getBytes());
            }
        } catch (IOException ex) {
            throw new RuntimeException("Image upload failed", ex);
        }

        ListingDTO dto = ListingDTO.builder()
                .listingName(listingName)
                .listingDescription(listingDescription)
                .price(price)
                .quantity(quantity)
                .imageUrl(imageUrl)
                .build();

        return ResponseEntity.ok(listingService.addEquipment(dto));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ListingDTO> updateEquipment(@PathVariable Long id,
                                                      @RequestParam("listingName") String listingName,
                                                      @RequestParam("listingDescription") String listingDescription,
                                                      @RequestParam("price") String price,
                                                      @RequestParam("quantity") Integer quantity,
                                                      @RequestPart(value = "image", required = false) MultipartFile image) {
        String imageUrl = null;
        try {
            if (image != null && !image.isEmpty()) {
                imageUrl = imgBBService.uploadImage(image.getBytes());
            }
        } catch (IOException ex) {
            throw new RuntimeException("Image upload failed", ex);
        }

        ListingDTO dto = ListingDTO.builder()
                .listingName(listingName)
                .listingDescription(listingDescription)
                .price(price)
                .quantity(quantity)
                .imageUrl(imageUrl) // can be null to preserve existing
                .build();

        return ResponseEntity.ok(listingService.updateEquipment(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEquipment(@PathVariable Long id) {
        listingService.deleteEquipment(id);
        return ResponseEntity.ok().body("{\"message\":\"Deleted successfully\"}");
    }

    @PostMapping("/{id}/purchase")
    public ResponseEntity<ListingDTO> purchase(@PathVariable Long id,
                                               @RequestParam(name = "quantity", defaultValue = "1") Integer quantity) {
        return ResponseEntity.ok(listingService.purchaseEquipment(id, quantity));
    }
}
