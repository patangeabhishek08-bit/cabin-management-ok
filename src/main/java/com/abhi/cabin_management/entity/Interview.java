package com.abhi.cabin_management.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "interviews")
@Data
public class Interview {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;


    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @ManyToOne
    @JoinColumn(name = "cabin_id")
    private Cabin cabin;

    @Enumerated(EnumType.STRING)
    private InterviewRound round;

    private Integer priority;

    private LocalDate interviewDate;

    private String timeSlot;

    @Enumerated(EnumType.STRING)
    private InterviewStatus status;

    private String remark;

    private LocalDateTime createdAt = LocalDateTime.now();
}
