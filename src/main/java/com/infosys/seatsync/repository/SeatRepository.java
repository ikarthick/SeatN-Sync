package com.infosys.seatsync.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.infosys.seatsync.entity.infra.Seat;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
   
	// JPQL versions
    @Query("SELECT s FROM Seat s WHERE s.wing.wingId = :wingId")
    List<Seat> getSeatsByWing(@Param("wingId") Long wingId);

    @Query("SELECT COUNT(s) FROM Seat s WHERE s.wing.wingId = :wingId")
    long countSeatsByWing(@Param("wingId") Long wingId);
    
}
