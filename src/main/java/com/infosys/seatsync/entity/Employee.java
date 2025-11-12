package com.infosys.seatsync.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Employee {
    @Id
    private String empId;  // Using company ID directly

    private String name;
    private String email;

    @Enumerated(EnumType.STRING)
    private AccessType odcAccessType;

    // Self-join for manager mapping
    @ManyToOne
    @JoinColumn(name = "manager_id")
    private Employee manager;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne
    @JoinColumn(name = "home_dc_id")
    private DeliveryCenter homeDC;
}
