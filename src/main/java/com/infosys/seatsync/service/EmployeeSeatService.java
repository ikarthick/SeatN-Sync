package com.infosys.seatsync.service;

import com.infosys.seatsync.dto.EmployeeSeatResponseDto;

public interface EmployeeSeatService {

    EmployeeSeatResponseDto employeeBookedSeats(String empId);
}
