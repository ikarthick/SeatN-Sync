package com.infosys.seatsync.service.impl;

import com.infosys.seatsync.dto.EmployeeSeatResponseDto;
import com.infosys.seatsync.dto.EmployeeSeatResponsePayloadDto;
import com.infosys.seatsync.dto.TeamProximitySugguestionDto;
import com.infosys.seatsync.entity.booking.Booking;
import com.infosys.seatsync.entity.emp.Employee;
import com.infosys.seatsync.entity.infra.Seat;
import com.infosys.seatsync.repository.EmployeeRepository;
import com.infosys.seatsync.repository.SeatBookingRepository;
import com.infosys.seatsync.service.EmployeeSeatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeSeatServiceImpl implements EmployeeSeatService {

    @Autowired
    SeatBookingRepository seatBookingRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    private static final Logger logger = LoggerFactory.getLogger(SeatsAvailabilityServiceImpl.class);

    @Override
    public EmployeeSeatResponseDto employeeBookedSeats(String empId) {
        EmployeeSeatResponseDto employeeSeatResponseDto = new EmployeeSeatResponseDto();

        Optional<List<Booking>> listOfBookings = seatBookingRepository.findByEmployee_EmpId(empId);
        if(listOfBookings.isEmpty()){
            employeeSeatResponseDto.setStatus("NO_BOOKING_FOUND");
            employeeSeatResponseDto.setMessage("There is no seat booking for employee Id: "+ empId);
            employeeSeatResponseDto.setTeamProximitySugguestionDto(checkTeamProximitySeats(empId));
            return employeeSeatResponseDto;
        } else {

            //Map the required fields based on booking details
            List<EmployeeSeatResponsePayloadDto> responseList =
                    listOfBookings.orElse(Collections.emptyList())
                            .stream()
                            .map(booking -> {
                                EmployeeSeatResponsePayloadDto dto = new EmployeeSeatResponsePayloadDto();

                                Seat seat = booking.getSeat();
                                dto.setCity(seat.getWing().getBlock().getDeliveryCenter().getLocation());
                                dto.setDcName(seat.getWing().getBlock().getDeliveryCenter().getDcName());
                                dto.setBlockName(seat.getWing().getBlock().getBlockName());
                                dto.setWingName(seat.getWing().getWingName());
                                dto.setSeatName(seat.getSeatCode());

                                dto.setBookingDate(booking.getBookingDate());
                                dto.setAccessType(booking.getSeat().getWing().getAccessType().toString());
                                dto.setStartTime(booking.getStartTime());
                                dto.setEndTime(booking.getEndTime());
                                return dto;
                            }).toList();

            employeeSeatResponseDto.setEmpSeats(responseList);
            employeeSeatResponseDto.setStatus("SUCCESS");
            employeeSeatResponseDto.setMessage("Employee seats has been successfully retrieved for employee Id: "+ empId);
            employeeSeatResponseDto.setTeamProximitySugguestionDto(checkTeamProximitySeats(empId));
            return employeeSeatResponseDto;
        }
    }

    private TeamProximitySugguestionDto checkTeamProximitySeats(String empId) {
        try {
            Optional<Employee> employeeOpl = employeeRepository.findById(empId);
            if(employeeOpl.isPresent()){
                String managerId = employeeOpl.get().getManager().getEmpId();
                Optional<List<Booking>> teamProximityBookings = seatBookingRepository.findByEmployee_Manager_EmpId(managerId);
                if(teamProximityBookings.isPresent() && !teamProximityBookings.get().isEmpty()){
                    Optional<TeamProximitySugguestionDto> teamProximitySuggestion = teamProximityBookings.get().stream().findFirst().map(booking -> {
                        TeamProximitySugguestionDto teamProximitySugguestionDto = new TeamProximitySugguestionDto();
                        teamProximitySugguestionDto.setDcName(booking.getSeat().getWing().getBlock().getDeliveryCenter().getDcName());
                        teamProximitySugguestionDto.setWingName(booking.getSeat().getWing().getWingName());
                        teamProximitySugguestionDto.setBlockName(booking.getSeat().getWing().getBlock().getBlockName());
                        return teamProximitySugguestionDto;
                    });
                    return teamProximitySuggestion.get();
                }
            } else {
                logger.error("Employee obj is not present");
                return null;
            }
        } catch (Exception exception){
            return null;
        }
        return null;
    }
}
