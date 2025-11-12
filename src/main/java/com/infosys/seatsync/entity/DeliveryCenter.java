package com.infosys.seatsync.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class DeliveryCenter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dcId;

    private String dcName;
    private String location;
}

