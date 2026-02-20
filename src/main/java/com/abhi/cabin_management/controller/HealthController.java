package com.abhi.cabin_management.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/")
    public String home() {
        return "Cabin Management API is running ✅";
    }

    @GetMapping("/health")
    public String health() {
        return "OK";
    }
}