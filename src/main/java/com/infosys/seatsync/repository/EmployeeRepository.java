package com.infosys.seatsync.repository;

import com.infosys.seatsync.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String> {

    List<Employee> findByManagerId(String managerId);

    List<Employee> findByProjectId(String projectId);

    Employee findByEmail(String email);
}
