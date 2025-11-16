package com.infosys.seatsync.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.infosys.seatsync.entity.booking.Booking;

@Repository
public interface SeatBookingRepository extends JpaRepository<Booking, Long> {
        
	@Query(value = "SELECT * FROM booking b " +
            "WHERE b.seat_id = :seatId " +
            "AND b.booking_date = :date " +
            "AND b.status IN ('BOOKED', 'CHECKED_IN')", 
    nativeQuery = true)	
	List<Booking> findBookingInfo(@Param("seatId") Long seatId, @Param("date") String date);

}
