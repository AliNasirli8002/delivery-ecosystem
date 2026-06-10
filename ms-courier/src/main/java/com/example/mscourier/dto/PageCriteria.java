package com.example.mscourier.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageCriteria {

    private Integer page;
    private Integer size;
    private List<String> sort; // This matches the array sorting structure from Swagger
}