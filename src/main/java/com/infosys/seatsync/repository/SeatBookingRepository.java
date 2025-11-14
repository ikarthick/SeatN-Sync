package com.infosys.seatsync.repository;

import com.infosys.seatsync.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeatBookingRepository extends JpaRepository<Booking, Long> {

}
