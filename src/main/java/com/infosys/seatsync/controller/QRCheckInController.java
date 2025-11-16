package com.infosys.seatsync.controller;

import com.infosys.seatsync.dto.qr.QRCheckInResponseDto;
import com.infosys.seatsync.service.QRCheckInService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/infy")
public class QRCheckInController {

    private QRCheckInService qrCheckInService;

    public QRCheckInController(QRCheckInService qrCheckInService) {
        this.qrCheckInService = qrCheckInService;
    }

    @GetMapping("/checkIn")
    public ResponseEntity<QRCheckInResponseDto> processQRCheckIn(@RequestParam String seatHashCode,
                                                                 @RequestParam String empId){
        return new ResponseEntity<>(qrCheckInService.processCheckIn(seatHashCode, empId), HttpStatus.OK);
    }

}
