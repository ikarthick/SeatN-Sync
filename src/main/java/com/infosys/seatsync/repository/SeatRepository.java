package com.infosys.seatsync.repository;

import com.infosys.seatsync.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatRepository extends JpaRepository<Seat, Long> {
}
