package com.example.mscourier.service;

import com.example.mscourier.dto.CourierCriteria;
import com.example.mscourier.dto.CourierResponse;
import com.example.mscourier.dto.CreateCourierRequest;
import com.example.mscourier.dto.PageCriteria;
import com.example.mscourier.entity.Courier;
import com.example.mscourier.enums.CourierStatus;
import com.example.mscourier.mapper.CourierMapper;
import com.example.mscourier.repository.CourierRepository;
import com.example.mscourier.repository.specification.CourierSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CourierService {

    private final CourierRepository courierRepository;
    private final CourierMapper courierMapper;

    public CourierResponse createCourier(CreateCourierRequest request) {
        Courier courierEntity = courierMapper.toEntity(request);
        Courier savedCourier = courierRepository.save(courierEntity);
        return courierMapper.toResponse(savedCourier);
    }

    @Transactional(readOnly = true)
    public CourierResponse getCourierById(Long id) {
        return courierRepository.findById(id)
                .map(courierMapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Courier not found with id: " + id));
    }

    public Page<CourierResponse> getAvailableCouriers(CourierCriteria courierCriteria, PageCriteria pageCriteria) {
        int page = (pageCriteria.getPage() != null) ? pageCriteria.getPage() : 0;
        int size = (pageCriteria.getSize() != null) ? pageCriteria.getSize() : 10;
        Pageable pageable = PageRequest.of(page, size);

        CourierSpecification spec = new CourierSpecification(courierCriteria);
        Page<Courier> courierPage = courierRepository.findAll(spec, pageable);

        return courierPage.map(courierMapper::toResponse);
    }

    public Page<CourierResponse> getAllCouriersWithFilter(CourierCriteria criteria, Pageable pageable) {
        CourierSpecification spec = new CourierSpecification(criteria);
        Page<Courier> courierPage = courierRepository.findAll(spec, pageable);
        return courierPage.map(courierMapper::toResponse);
    }

    @Transactional
    public void updateCourierStatus(Long id, CourierStatus newStatus) {
        Courier courier = courierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Courier not found with id: " + id));

        courier.setStatus(newStatus);
        courierRepository.save(courier);
    }
}