package com.infosys.seatsync.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "waitlist")
public class Waitlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Employee waiting for seat
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    // The seat/block/DC the employee requested
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "block_id", nullable = false)
    private Block block;

    @Column(name = "priority_position")
    private int priorityPosition; // e.g., 1 for WL1, 2 for WL2, etc.

    @Column(name = "requested_date")
    private LocalDateTime requestedDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private WaitlistStatus status;

    @Column(name = "remarks")
    private String remarks;

    public Waitlist() {}

    public Waitlist(Employee employee, Block block, int priorityPosition, LocalDateTime requestedDate, WaitlistStatus status) {
        this.employee = employee;
        this.block = block;
        this.priorityPosition = priorityPosition;
        this.requestedDate = requestedDate;
        this.status = status;
    }

    // Getters and Setters
    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public Employee getEmployee() { return employee; }

    public void setEmployee(Employee employee) { this.employee = employee; }

    public Block getBlock() { return block; }

    public void setBlock(Block block) { this.block = block; }

    public int getPriorityPosition() { return priorityPosition; }

    public void setPriorityPosition(int priorityPosition) { this.priorityPosition = priorityPosition; }

    public LocalDateTime getRequestedDate() { return requestedDate; }

    public void setRequestedDate(LocalDateTime requestedDate) { this.requestedDate = requestedDate; }

    public WaitlistStatus getStatus() { return status; }

    public void setStatus(WaitlistStatus status) { this.status = status; }

    public String getRemarks() { return remarks; }

    public void setRemarks(String remarks) { this.remarks = remarks; }

    public enum WaitlistStatus {
        ACTIVE,          // Currently on waitlist
        PROMOTED,        // Moved up (e.g., from WL4 → WL3)
        CONFIRMED,       // Got a seat
        CANCELLED,       // Cancelled by user
        EXPIRED          // Timed out or no longer relevant
    }

}

