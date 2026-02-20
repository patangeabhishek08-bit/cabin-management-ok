package com.abhi.cabin_management.service;

import com.abhi.cabin_management.entity.Interview;
import com.abhi.cabin_management.entity.Student;
import com.abhi.cabin_management.repository.StudentRepository;
import com.abhi.cabin_management.repository.InterviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final InterviewRepository interviewRepository;

    public Student addStudent(Student student) {
        return studentRepository.save(student);
    }

    public List<Interview> getStudentHistory(String studentId) {
        return interviewRepository.findByStudentId(UUID.fromString(studentId));
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }
}
