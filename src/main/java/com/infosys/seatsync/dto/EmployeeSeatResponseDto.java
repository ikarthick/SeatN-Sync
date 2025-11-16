package com.infosys.seatsync.dto;

import java.util.List;

public class EmployeeSeatResponseDto {
    private List<EmployeeSeatResponsePayloadDto> empSeats;
    private TeamProximitySugguestionDto teamProximitySugguestionDto;
    private String status;
    private String message;

    public TeamProximitySugguestionDto getTeamProximitySugguestionDto() {
        return teamProximitySugguestionDto;
    }

    public void setTeamProximitySugguestionDto(TeamProximitySugguestionDto teamProximitySugguestionDto) {
        this.teamProximitySugguestionDto = teamProximitySugguestionDto;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<EmployeeSeatResponsePayloadDto> getEmpSeats() {
        return empSeats;
    }

    public void setEmpSeats(List<EmployeeSeatResponsePayloadDto> empSeats) {
        this.empSeats = empSeats;
    }
}
