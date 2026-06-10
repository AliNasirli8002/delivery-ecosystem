package com.example.mscourier.mapper;

import com.example.mscourier.dto.CreateCourierRequest;
import com.example.mscourier.dto.CourierResponse;
import com.example.mscourier.entity.Courier;
import com.example.mscourier.enums.CourierStatus;
import org.springframework.stereotype.Component;

@Component
public class CourierMapper {

    public Courier toEntity(CreateCourierRequest dto) {
        if (dto == null) return null;

        return Courier.builder()
                .name(dto.getName())
                .status(CourierStatus.FREE) // Automatically defaults new couriers to FREE status
                .build();
    }

    public CourierResponse toResponse(Courier entity) {
        if (entity == null) return null;

        return CourierResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .status(entity.getStatus())
                .build();
    }
}