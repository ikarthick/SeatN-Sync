package com.infosys.seatsync.entity.emp;

import java.util.Objects;

import com.infosys.seatsync.entity.infra.DeliveryCenter;
import com.infosys.seatsync.entity.infra.Wing;
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
    private Wing.AccessType odcAccessType;

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

    public Employee(String empId, String name, String email, Wing.AccessType odcAccessType, Employee manager, Project project, DeliveryCenter homeDC) {
        this.empId = empId;
        this.name = name;
        this.email = email;
        this.odcAccessType = odcAccessType;
        this.manager = manager;
        this.project = project;
        this.homeDC = homeDC;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "empId='" + empId + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", odcAccessType=" + odcAccessType +
                ", project=" + project +
                ", homeDC=" + homeDC +
                '}';
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Wing.AccessType getOdcAccessType() {
        return odcAccessType;
    }

    public void setOdcAccessType(Wing.AccessType odcAccessType) {
        this.odcAccessType = odcAccessType;
    }

    public Employee getManager() {
        return manager;
    }

    public void setManager(Employee manager) {
        this.manager = manager;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public DeliveryCenter getHomeDC() {
        return homeDC;
    }

    public void setHomeDC(DeliveryCenter homeDC) {
        this.homeDC = homeDC;
    }

    public Employee() {
    }
}
