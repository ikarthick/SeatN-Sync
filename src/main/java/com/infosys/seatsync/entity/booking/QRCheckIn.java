package com.infosys.seatsync.entity.booking;

import com.infosys.seatsync.entity.emp.Employee;
import com.infosys.seatsync.entity.infra.Seat;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class QRCheckIn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long checkinId;

    private LocalDateTime checkinTime;

    @ManyToOne
    @JoinColumn(name = "seat_id")
    private Seat seat;

    @ManyToOne
    @JoinColumn(name = "emp_id")
    private Employee employee;

    @OneToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;

	public Long getCheckinId() {
		return checkinId;
	}

	public void setCheckinId(Long checkinId) {
		this.checkinId = checkinId;
	}

	public LocalDateTime getCheckinTime() {
		return checkinTime;
	}

	public void setCheckinTime(LocalDateTime checkinTime) {
		this.checkinTime = checkinTime;
	}

	public Seat getSeat() {
		return seat;
	}

	public void setSeat(Seat seat) {
		this.seat = seat;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Booking getBooking() {
		return booking;
	}

	public void setBooking(Booking booking) {
		this.booking = booking;
	}
}
