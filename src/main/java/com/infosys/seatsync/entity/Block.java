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

    @ManyToOne
    @JoinColumn(name = "dc_id")
    private DeliveryCenter deliveryCenter;

    public Block() {
    }
}

