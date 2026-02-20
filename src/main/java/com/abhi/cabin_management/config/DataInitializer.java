package com.abhi.cabin_management.config;

import com.abhi.cabin_management.entity.Cabin;
import com.abhi.cabin_management.repository.CabinRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final CabinRepository cabinRepository;

    @PostConstruct
    public void init() {

        if (cabinRepository.count() == 0) {

            for (int i = 1; i <= 3; i++) {
                Cabin cabin = new Cabin();
                cabin.setCabinNumber(i);
                cabin.setActive(true);
                cabinRepository.save(cabin);
            }

            System.out.println("3 Cabins Created Successfully!");
        }
    }
}
