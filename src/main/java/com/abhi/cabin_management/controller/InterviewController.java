package com.abhi.cabin_management.controller;

import com.abhi.cabin_management.entity.Interview;
import com.abhi.cabin_management.entity.InterviewRound;
import com.abhi.cabin_management.service.InterviewService;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/interviews")
@RequiredArgsConstructor
public class InterviewController {

    private final InterviewService interviewService;

    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getInterviewStatus(
            @RequestParam LocalDate date,
            @RequestParam String timeSlot
    ) {
        return ResponseEntity.ok(interviewService.getInterviewStatus(date, timeSlot));
    }

    @PostMapping("/book")
    public ResponseEntity<Map<String, Object>> bookInterview(
            @RequestBody BookInterviewRequest request
    ) {
        InterviewRound round = parseRoundType(request.getRoundType());

        Interview interview = interviewService.bookInterview(
                request.getStudentId(),
                request.getCompanyId(),
                round,
                request.getDate(),
                request.getTimeSlot()
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of(
                        "message", "Interview booked successfully",
                        "interview", interview
                ));
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<Map<String, Object>> completeInterview(
            @PathVariable String id
    ) {
        Interview interview = interviewService.completeInterview(id);

        return ResponseEntity.ok(Map.of(
                "message", "Interview marked as completed",
                "interview", interview
        ));
    }

    private InterviewRound parseRoundType(String roundType) {
        if (roundType == null || roundType.isBlank()) {
            throw new IllegalArgumentException("roundType is required");
        }

        String normalized = roundType.trim().toUpperCase();

        return switch (normalized) {
            case "TECHNICAL" -> InterviewRound.ROUND_1;
            default -> InterviewRound.valueOf(normalized);
        };
    }

    @Data
    public static class BookInterviewRequest {
        @NotBlank
        private String studentId;

        @NotBlank
        private String companyId;

        private LocalDate date;

        @NotBlank
        private String timeSlot;

        @NotBlank
        private String roundType;
    }
}
