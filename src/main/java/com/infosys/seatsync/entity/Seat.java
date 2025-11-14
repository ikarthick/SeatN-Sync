package com.infosys.seatsync.entity;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seatId;

    private String seatCode;
    private String cubicleId;
    private Boolean isAvailable = true;

    @ManyToOne
    @JoinColumn(name = "block_id")
    private Block block;
}
