package com.example.mscourier.dto;

import com.example.mscourier.enums.CourierStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourierResponse {
    private Long id;
    private String name;
    private CourierStatus status;
}