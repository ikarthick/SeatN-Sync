package com.infosys.seatsync.service;

import com.infosys.seatsync.dto.qr.QRCheckInResponseDto;

public interface QRCheckInService {
    QRCheckInResponseDto processCheckIn(String seatHashCode, String empId);
}
