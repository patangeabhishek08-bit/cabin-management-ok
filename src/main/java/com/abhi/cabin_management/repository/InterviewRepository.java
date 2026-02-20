package com.abhi.cabin_management.repository;

import com.abhi.cabin_management.entity.Cabin;
import com.abhi.cabin_management.entity.Interview;
import com.abhi.cabin_management.entity.InterviewStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface InterviewRepository extends JpaRepository<Interview, UUID> {
    long countByInterviewDate(LocalDate interviewDate);

    long countByInterviewDateAndStatus(LocalDate interviewDate, InterviewStatus status);


    boolean existsByCabinAndInterviewDateAndTimeSlot(
            Cabin cabin,
            LocalDate interviewDate,
            String timeSlot
    );
    List<Interview> findByInterviewDate(LocalDate interviewDate);
    List<Interview> findByStudentId(UUID studentId);
    List<Interview> findByStudentIdAndInterviewDateAndTimeSlot(
            UUID studentId,
            LocalDate date,
            String timeSlot
    );

    List<Interview> findByInterviewDateAndTimeSlot(LocalDate date, String timeSlot);
}
