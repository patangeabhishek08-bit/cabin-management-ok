package com.abhi.cabin_management.controller;

import com.abhi.cabin_management.service.InterviewService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/interviews")
public class InterviewController {

    private final InterviewService interviewService;

    public InterviewController(InterviewService interviewService) {
        this.interviewService = interviewService;
    }

    @GetMapping("/status")
    public Map<String, Object> getStatus(
            @RequestParam LocalDate date,
            @RequestParam String timeSlot) {

        return interviewService.getInterviewStatus(date, timeSlot);
    }
}