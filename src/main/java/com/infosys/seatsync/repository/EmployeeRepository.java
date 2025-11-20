package com.infosys.seatsync.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.infosys.seatsync.entity.emp.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String> {

	String findManager_empIdByEmpId(String employee_id);

	List<Long> findEmpIdByManagerEmpId(String managerId);

}
