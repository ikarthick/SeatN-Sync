package com.infosys.seatsync.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Block {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long blockId;

    private String blockName;
    private Integer totalSeats;

    @Enumerated(EnumType.STRING)
    private AccessType accessType;

    @ManyToOne
    @JoinColumn(name = "dc_id")
    private DeliveryCenter deliveryCenter;

    public Block() {
    }

    public enum AccessType {
        RESTRICTED, UNRESTRICTED
    }
}

