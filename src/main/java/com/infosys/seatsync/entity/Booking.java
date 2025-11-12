package com.infosys.seatsync.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;

    private String bookingDate;
    private String startTime;
    private String endTime;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    @ManyToOne
    @JoinColumn(name = "emp_id")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "seat_id")
    private Seat seat;

    @ManyToOne
    @JoinColumn(name = "booked_by")
    private Employee bookedBy;  // Team lead booking

    public enum BookingStatus {
        BOOKED, CHECKED_IN, RELEASED, AUTO_RELEASED, CANCELLED
    }

}

