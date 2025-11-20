package com.infosys.seatsync.repository;

import java.util.List;
import java.util.Optional;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
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

	Optional<Booking> findByEmployee_EmpIdAndSeat_HashCodeAndBookingDateAndStatus(
			String empId,
			String hashCode,
			String bookingDate,
			Booking.BookingStatus status
	);

	@Query("SELECT b FROM Booking b WHERE b.status = 'BOOKED' AND b.bookingDate = :today")
	List<Booking> findTodayBooked(@Param("today") String today);

	Optional<List<Booking>> findByEmployee_EmpId(String empId);

	Optional<List<Booking>> findByEmployee_Manager_EmpId(String managerId);

	//List<Booking> findAllByBookingDateAndBookedBy_EmpId(String bookingDate, Long empId);

	//List<Booking> findStatusBySeat(Long s);

	@Query("select b from Booking b where b.employee.manager.empId = :managerId and b.seat.wing.wingId = :wingId and b.bookingDate = :date and b.status IN ('BOOKED', 'CHECKED_IN')")
			List<Booking> findBookings(
			@Param("managerId") String managerId,
			@Param("date") String date,
			@Param("wingId") Long wingId
	);

}
