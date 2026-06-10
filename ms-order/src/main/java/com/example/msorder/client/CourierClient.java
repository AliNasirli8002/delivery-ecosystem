package com.example.msorder.client;

import com.example.msorder.client.dto.CourierResponseDto;
import com.example.msorder.criteria.PageCriteria;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-courier", url = "http://localhost:8082/api/v1/couriers") // Adjust name/url to match yours
public interface CourierClient {

    @GetMapping("/available")
    Page<CourierResponseDto> getAvailableCouriers(@SpringQueryMap PageCriteria criteria);

    @GetMapping("/{id}")
    CourierResponseDto getCourierById(@PathVariable("id") Long id);
}