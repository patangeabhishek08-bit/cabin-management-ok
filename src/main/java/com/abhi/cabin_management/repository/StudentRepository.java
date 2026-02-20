package com.abhi.cabin_management.repository;

import com.abhi.cabin_management.entity.Interview;
import com.abhi.cabin_management.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface StudentRepository extends JpaRepository<Student, UUID> {


}
