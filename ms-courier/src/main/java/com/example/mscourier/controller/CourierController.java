package com.example.mscourier.controller;

import com.example.mscourier.dto.CourierCriteria;
import com.example.mscourier.dto.CourierResponse;
import com.example.mscourier.dto.CreateCourierRequest;
import com.example.mscourier.dto.PageCriteria;
import com.example.mscourier.service.CourierService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/couriers")
@RequiredArgsConstructor
public class CourierController {

    private final CourierService courierService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CourierResponse createCourier(@Valid @RequestBody CreateCourierRequest request) {
        return courierService.createCourier(request);
    }

    @GetMapping("/available")
    public ResponseEntity<Page<CourierResponse>> getAvailableCouriers(CourierCriteria criteria) {
        Pageable topOne = PageRequest.of(0, 1);
        Page<CourierResponse> availableCouriers = courierService.getAllCouriersWithFilter(criteria, topOne);
        return ResponseEntity.ok(availableCouriers);
    }

    // 🚀 NEW: Add this endpoint so ms-order can query specific couriers by ID!
    @GetMapping("/{id}")
    public ResponseEntity<CourierResponse> getCourierById(@PathVariable Long id) {
        // Calling your service layer logic to find the single courier record
        CourierResponse courier = courierService.getCourierById(id);
        return ResponseEntity.ok(courier);
    }

    @GetMapping
    public ResponseEntity<Page<CourierResponse>> getAllCouriers(CourierCriteria criteria, PageCriteria pageCriteria) {
        int page = (pageCriteria.getPage() != null) ? pageCriteria.getPage() : 0;
        int size = (pageCriteria.getSize() != null) ? pageCriteria.getSize() : 10;
        Sort fallbackSort = Sort.by("id").ascending();

        Pageable cleanPageable;

        if (pageCriteria.getSort() != null && !pageCriteria.getSort().isEmpty()) {
            boolean hasInvalidSort = pageCriteria.getSort().stream()
                    .anyMatch(sortStr -> sortStr == null
                            || sortStr.trim().isEmpty()
                            || "string".equalsIgnoreCase(sortStr.trim())
                            || sortStr.contains("\"")
                            || sortStr.contains("'"));

            if (hasInvalidSort) {
                cleanPageable = PageRequest.of(page, size, fallbackSort);
            } else {
                String validProperty = pageCriteria.getSort().getFirst().trim();
                cleanPageable = PageRequest.of(page, size, Sort.by(validProperty).ascending());
            }
        } else {
            cleanPageable = PageRequest.of(page, size, fallbackSort);
        }

        Page<CourierResponse> couriers = courierService.getAllCouriersWithFilter(criteria, cleanPageable);
        return ResponseEntity.ok(couriers);
    }
}