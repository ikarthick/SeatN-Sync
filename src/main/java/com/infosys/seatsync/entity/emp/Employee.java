package com.infosys.seatsync.entity.emp;

import java.util.Objects;

import com.infosys.seatsync.entity.infra.DeliveryCenter;

import jakarta.persistence.AccessType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

	public AccessType getOdcAccessType() {
		return odcAccessType;
	}

	public void setOdcAccessType(AccessType odcAccessType) {
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

	@Override
	public String toString() {
		return "Employee [empId=" + empId + ", name=" + name + ", email=" + email + ", odcAccessType=" + odcAccessType
				+ ", manager=" + manager + ", project=" + project + ", homeDC=" + homeDC + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(email, empId, homeDC, manager, name, odcAccessType, project);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Employee other = (Employee) obj;
		return Objects.equals(email, other.email) && Objects.equals(empId, other.empId)
				&& Objects.equals(homeDC, other.homeDC) && Objects.equals(manager, other.manager)
				&& Objects.equals(name, other.name) && odcAccessType == other.odcAccessType
				&& Objects.equals(project, other.project);
	}
}
