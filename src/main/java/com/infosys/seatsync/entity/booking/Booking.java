package com.infosys.seatsync.entity.booking;

import com.infosys.seatsync.entity.emp.Employee;
import com.infosys.seatsync.entity.infra.Seat;
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
    
    private Boolean isfullDayBooked=false;
    
    private Boolean isSlotABooked=false;
    
    private Boolean isSlotBBooked=false;
    
    private Boolean isSlotCBooked=false;
 
    public enum BookingStatus {
        BOOKED, CHECKED_IN, RELEASED, AUTO_RELEASED, CANCELLED
    }

	public Long getBookingId() {
		return bookingId;
	}

	public void setBookingId(Long bookingId) {
		this.bookingId = bookingId;
	}

	public String getBookingDate() {
		return bookingDate;
	}

	public void setBookingDate(String bookingDate) {
		this.bookingDate = bookingDate;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public BookingStatus getStatus() {
		return status;
	}

	public void setStatus(BookingStatus status) {
		this.status = status;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Seat getSeat() {
		return seat;
	}

	public void setSeat(Seat seat) {
		this.seat = seat;
	}

	public Employee getBookedBy() {
		return bookedBy;
	}

	public void setBookedBy(Employee bookedBy) {
		this.bookedBy = bookedBy;
	}

	public Boolean getIsfullDayBooked() {
		return isfullDayBooked;
	}

	public void setIsfullDayBooked(Boolean isfullDayBooked) {
		this.isfullDayBooked = isfullDayBooked;
	}

	public Boolean getIsSlotABooked() {
		return isSlotABooked;
	}

	public void setIsSlotABooked(Boolean isSlotABooked) {
		this.isSlotABooked = isSlotABooked;
	}

	public Boolean getIsSlotBBooked() {
		return isSlotBBooked;
	}

	public void setIsSlotBBooked(Boolean isSlotBBooked) {
		this.isSlotBBooked = isSlotBBooked;
	}

	public Boolean getIsSlotCBooked() {
		return isSlotCBooked;
	}

	public void setIsSlotCBooked(Boolean isSlotCBooked) {
		this.isSlotCBooked = isSlotCBooked;
	}

}