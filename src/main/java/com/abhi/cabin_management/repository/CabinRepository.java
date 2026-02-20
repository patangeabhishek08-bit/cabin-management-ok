package com.abhi.cabin_management.repository;

import com.abhi.cabin_management.entity.Cabin;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface CabinRepository extends JpaRepository<Cabin, UUID> {
}
