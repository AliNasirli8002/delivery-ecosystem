package com.example.msorder.client.dto;

import com.example.msorder.client.enums.CourierStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourierResponseDto {
    private Long id;
    private String name;
    private CourierStatus status;
}