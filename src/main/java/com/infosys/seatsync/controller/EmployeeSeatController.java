package com.infosys.seatsync.controller;

import com.infosys.seatsync.dto.AttendanceResponseDto;
import com.infosys.seatsync.dto.EmployeeSeatResponseDto;
import com.infosys.seatsync.service.EmployeeSeatService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/infy/emp")
public class EmployeeSeatController {
    private EmployeeSeatService employeeSeatService;

    public EmployeeSeatController(EmployeeSeatService employeeSeatService) {
        this.employeeSeatService = employeeSeatService;
    }

    @PostMapping("/{empId}/seat")
    public ResponseEntity<EmployeeSeatResponseDto> markAttendanceForEmp(@PathVariable String empId){
        return new ResponseEntity<>(employeeSeatService.employeeBookedSeats(empId), HttpStatus.OK);
    }
}
