package com.infosys.seatsync.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.infosys.seatsync.entity.infra.Seat;

import jakarta.persistence.LockModeType;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
   
	// JPQL versions
    @Query("SELECT s FROM Seat s WHERE s.wing.wingId = :wingId")
    List<Seat> getSeatsByWing(@Param("wingId") Long wingId);

//    @Query("SELECT COUNT(s) FROM Seat s WHERE s.wing.wingId = :wingId")
//    long countSeatsByWing(@Param("wingId") Long wingId);

//	List<Long> findSeatIdByCubicleId(String c);
    
//	@Lock(LockModeType.PESSIMISTIC_WRITE)
//	@Query("select s from Seat s where s.cubicleId = :cubicleId and s.wing.wingId = :wingId and s.isAvailable = true")
//	List<Seat> findAvailableSeatsForCubicleForUpdate(@Param("cubicleId") String cubicleId,
//	                                              @Param("wingId") Long wingId);
//
//	@Lock(LockModeType.PESSIMISTIC_WRITE)
//	@Query("SELECT s FROM Seat s " +
//	       "WHERE s.wing.wingId = :wingId " +
//	       "AND s.isAvailable = true")
//	List<Seat> findAvailableSeatsInWingForUpdate(@Param("wingId") Long wingId);

}
