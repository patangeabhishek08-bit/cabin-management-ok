package com.abhi.cabin_management.service;

import com.abhi.cabin_management.dto.DashboardSummaryDTO;
import com.abhi.cabin_management.entity.*;
import com.abhi.cabin_management.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InterviewService {

    private final InterviewRepository interviewRepository;
    private final StudentRepository studentRepository;
    private final CompanyRepository companyRepository;
    private final CabinRepository cabinRepository;

    public List<Interview> getTodaysInterviews() {
        return interviewRepository.findByInterviewDate(LocalDate.now());
    }
    public List<Interview> getCabinStatus(LocalDate date, String timeSlot) {

        timeSlot = timeSlot.trim();

        return interviewRepository.findByInterviewDateAndTimeSlot(date, timeSlot);
    }
    public DashboardSummaryDTO getDashboardSummary() {

        LocalDate today = LocalDate.now();

        long totalInterviews = interviewRepository.countByInterviewDate(today);

        long scheduled = interviewRepository
                .countByInterviewDateAndStatus(today, InterviewStatus.SCHEDULED);

        long waiting = interviewRepository
                .countByInterviewDateAndStatus(today, InterviewStatus.WAITING);

        long completed = interviewRepository
                .countByInterviewDateAndStatus(today, InterviewStatus.COMPLETED);

        long totalStudents = studentRepository.count();
        long totalCompanies = companyRepository.count();

        return new DashboardSummaryDTO(
                totalInterviews,
                scheduled,
                waiting,
                completed,
                totalStudents,
                totalCompanies
        );
    }

    public Interview completeInterview(String interviewId) {

        Interview interview = interviewRepository.findById(
                java.util.UUID.fromString(interviewId)
        ).orElseThrow();

        interview.setStatus(InterviewStatus.COMPLETED);

        Cabin freedCabin = interview.getCabin();
        interview.setCabin(null);

        interviewRepository.save(interview);

        // Promote waiting candidate
        promoteWaitingInterview(
                interview.getInterviewDate(),
                interview.getTimeSlot().trim(),
                freedCabin
        );

        return interview;
    }
    private void promoteWaitingInterview(
            java.time.LocalDate date,
            String timeSlot,
            Cabin freedCabin
    ) {

        List<Interview> waitingInterviews =
                interviewRepository.findByInterviewDateAndTimeSlot(date, timeSlot)
                        .stream()
                        .filter(i -> i.getStatus() == InterviewStatus.WAITING)
                        .toList();

        if (waitingInterviews.isEmpty()) return;

        // Get highest priority waiting
        Interview highestPriority = waitingInterviews.stream()
                .max((i1, i2) -> Integer.compare(i1.getPriority(), i2.getPriority()))
                .orElse(null);

        if (highestPriority != null) {

            highestPriority.setCabin(freedCabin);
            highestPriority.setStatus(InterviewStatus.SCHEDULED);

            interviewRepository.save(highestPriority);
        }
    }


    public Interview bookInterview(
            String studentId,
            String companyId,
            InterviewRound round,
            LocalDate date,
            String timeSlot
    ) {
        timeSlot = timeSlot.trim();

        Student student = studentRepository.findById(java.util.UUID.fromString(studentId))
                .orElseThrow();

        Company company = companyRepository.findById(java.util.UUID.fromString(companyId))
                .orElseThrow();

        int priority = getPriority(round);
        // 🔒 Prevent duplicate booking for same student, same date, same time slot
        List<Interview> studentExisting =
                interviewRepository.findByStudentIdAndInterviewDateAndTimeSlot(
                        student.getId(),
                        date,
                        timeSlot
                );

        if (!studentExisting.isEmpty()) {
            throw new RuntimeException(
                    "Student already has an interview in this time slot"
            );
        }


        List<Interview> existingInterviews =
                interviewRepository.findByInterviewDateAndTimeSlot(date, timeSlot);

        List<Cabin> cabins = cabinRepository.findAll();

        // Case 1: If less than 3 interviews → assign normally
        if (existingInterviews.size() < cabins.size()) {

            for (Cabin cabin : cabins) {

                boolean occupied = existingInterviews.stream()
                        .anyMatch(i -> i.getCabin() != null &&
                                i.getCabin().getId().equals(cabin.getId()));

                if (!occupied) {

                    Interview interview = new Interview();
                    interview.setStudent(student);
                    interview.setCompany(company);
                    interview.setCabin(cabin);
                    interview.setRound(round);
                    interview.setPriority(priority);
                    interview.setInterviewDate(date);
                    interview.setTimeSlot(timeSlot);
                    interview.setStatus(InterviewStatus.SCHEDULED);

                    return interviewRepository.save(interview);
                }
            }
        }

        // Case 2: All cabins full → check priority replacement

        Interview lowestPriorityInterview = existingInterviews.stream()
                .min((i1, i2) -> Integer.compare(i1.getPriority(), i2.getPriority()))
                .orElse(null);

        if (lowestPriorityInterview != null &&
                lowestPriorityInterview.getPriority() < priority) {

            Cabin reassignedCabin = lowestPriorityInterview.getCabin();

            // Move old interview to WAITING
            lowestPriorityInterview.setCabin(null);
            lowestPriorityInterview.setStatus(InterviewStatus.WAITING);
            interviewRepository.save(lowestPriorityInterview);

            // Assign cabin to new high priority interview
            Interview newInterview = new Interview();
            newInterview.setStudent(student);
            newInterview.setCompany(company);
            newInterview.setCabin(reassignedCabin);
            newInterview.setRound(round);
            newInterview.setPriority(priority);
            newInterview.setInterviewDate(date);
            newInterview.setTimeSlot(timeSlot);
            newInterview.setStatus(InterviewStatus.SCHEDULED);

            return interviewRepository.save(newInterview);
        }

        // Case 3: Not high enough priority → WAITING
        Interview waitingInterview = new Interview();
        waitingInterview.setStudent(student);
        waitingInterview.setCompany(company);
        waitingInterview.setRound(round);
        waitingInterview.setPriority(priority);
        waitingInterview.setInterviewDate(date);
        waitingInterview.setTimeSlot(timeSlot);
        waitingInterview.setStatus(InterviewStatus.WAITING);

        return interviewRepository.save(waitingInterview);
    }

    private int getPriority(InterviewRound round) {
        return switch (round) {
            case FINAL -> 3;
            case ROUND_2 -> 2;
            case ROUND_1 -> 1;
        };
    }

    public List<Interview> getStudentHistory(String studentId) {
        return interviewRepository.findByStudentId(
                java.util.UUID.fromString(studentId)
        );
    }

}
