package com.infosys.seatsync.entity.booking;

import com.infosys.seatsync.entity.emp.Employee;
import com.infosys.seatsync.entity.infra.Wing;
import jakarta.persistence.*;

@Entity
@Table(name = "waitlist")
public class WaitList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Employee waiting for seat
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wing_id", nullable = false)
    private Wing wing;

    @Column(name = "priority")
    private int priority; // e.g., 1 for WL1, 2 for WL2, etc.

    @Column(name = "booking_date")
    private String bookingDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private WaitlistStatus status;

    @Column(name = "remarks")
    private String remarks;

    public String getBookingDate() {
        return bookingDate;
    }

    public WaitList(Long id, Employee employee, Wing wing, int priority, String bookingDate, WaitlistStatus status, String remarks) {
        this.id = id;
        this.employee = employee;
        this.wing = wing;
        this.priority = priority;
        this.bookingDate = bookingDate;
        this.status = status;
        this.remarks = remarks;
    }

    public Wing getWing() {
        return wing;
    }

    public void setWing(Wing wing) {
        this.wing = wing;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public WaitList() {}
    // Getters and Setters
    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public Employee getEmployee() { return employee; }

    public void setEmployee(Employee employee) { this.employee = employee; }

    public int getPriority() { return priority; }

    public void setPriority(int priority) { this.priority = priority; }
    public WaitlistStatus getStatus() { return status; }

    public void setStatus(WaitlistStatus status) { this.status = status; }

    public String getRemarks() { return remarks; }

    public void setRemarks(String remarks) { this.remarks = remarks; }

    public enum WaitlistStatus {
        WAITING,
        ALLOTTED,
        ACTIVE,          // Currently on waitlist
        PROMOTED,        // Moved up (e.g., from WL4 → WL3)
        CONFIRMED,       // Got a seat
        CANCELLED,       // Cancelled by user
        EXPIRED          // Timed out or no longer relevant
    }

}

