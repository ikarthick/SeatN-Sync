package com.infosys.seatsync.service.impl;

import com.infosys.seatsync.dto.qr.QRCheckInResponseDto;
import com.infosys.seatsync.repository.SeatBookingRepository;
import com.infosys.seatsync.service.QRCheckInService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class QRCheckInServiceImpl implements QRCheckInService {

    @Autowired
    SeatBookingRepository seatBookingRepository;

    @Override
    public QRCheckInResponseDto processCheckIn(String seatHashCode, String emp) {
        return null;
    }

    private QRCheckInResponseDto constructQRCheckInSuccessResponse(){
        QRCheckInResponseDto qrCheckInResponseDto = new QRCheckInResponseDto();
        qrCheckInResponseDto.setStatus("SUCCESS");
        qrCheckInResponseDto.setMessage("Employee Seat has been successfully checked In");
        qrCheckInResponseDto.setTimestamp(Instant.now());
        return qrCheckInResponseDto;
    }


}
