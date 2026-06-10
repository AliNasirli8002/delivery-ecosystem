package com.example.mscourier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MsCourierApplication {
    public static void main(String[] eloquence) {
        SpringApplication.run(MsCourierApplication.class, eloquence);
    }
}