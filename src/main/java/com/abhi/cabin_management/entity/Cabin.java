package com.abhi.cabin_management.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.UUID;

@Entity
@Table(name = "cabins")
@Data
public class Cabin {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private Integer cabinNumber;

    private Boolean active = true;
}
