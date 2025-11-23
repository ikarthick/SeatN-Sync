package com.infosys.seatsync.service.impl;

import com.infosys.seatsync.dto.qr.QRCheckInResponseDto;
import com.infosys.seatsync.entity.booking.Booking;
import com.infosys.seatsync.entity.booking.QRCheckIn;
import com.infosys.seatsync.entity.emp.Employee;
import com.infosys.seatsync.exception.BusinessException;
import com.infosys.seatsync.repository.AttendanceLogRepository;
import com.infosys.seatsync.repository.EmployeeRepository;
import com.infosys.seatsync.repository.QRCheckInRepository;
import com.infosys.seatsync.repository.SeatBookingRepository;
import com.infosys.seatsync.service.QRCheckInService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class QRCheckInServiceImpl implements QRCheckInService {

    @Autowired
    SeatBookingRepository seatBookingRepository;

    @Autowired
    QRCheckInRepository qrCheckInRepository;

    @Autowired
    AttendanceLogRepository attendanceLogRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    private static final Logger logger = LoggerFactory.getLogger(QRCheckInServiceImpl.class);

    @Transactional
    @Override
    public QRCheckInResponseDto processCheckIn(String seatHashCode, String empId) {
        try {
            boolean employeeExist = employeeRepository.existsById(empId);
            logger.info("Employee Exists: "+ employeeExist);
            if(!employeeExist){
                throw new BusinessException("EMP_NOT_FOUND",
                        "There is no Employee record found");
            }

            LocalDate today = LocalDate.now();
            LocalDateTime startOfDay = today.atStartOfDay();
            LocalDateTime endOfDay = today.atTime(23, 59, 59);


            //level 1 - Check if the emp has attendance record for the same date
            boolean isAttendanceExists = attendanceLogRepository.existsByEmployee_EmpIdAndSwipeTimeBetween(empId, startOfDay, endOfDay);
            logger.info("Attendance Exists: "+ isAttendanceExists);
            if(isAttendanceExists) {
                //level 2 - check if the emp has already had an active booking
                Optional<Booking> optionalBooking = seatBookingRepository.findByEmployee_EmpIdAndSeat_HashCodeAndBookingDateAndStatus(empId, seatHashCode, getCurrentDate(), Booking.BookingStatus.BOOKED);
                if(optionalBooking.isPresent()){
                    //level 3 - check if the QR checkIn is between the buffer period
                    boolean qrScanWithInTimeSlot = isWithinCheckInBuffer(optionalBooking.get().getStartTime(), 45);
                    if(qrScanWithInTimeSlot){

                        //Update booking status
                        Booking updatedBooking = optionalBooking.get();
                        updatedBooking.setStatus(Booking.BookingStatus.CHECKED_IN);
                        seatBookingRepository.save(updatedBooking);

                        //QR CheckIn Record
                        QRCheckIn qrCheckIn = new QRCheckIn();
                        qrCheckIn.setBooking(updatedBooking);
                        qrCheckIn.setCheckinTime(LocalDateTime.now());
                        qrCheckIn.setEmployee(optionalBooking.get().getEmployee());
                        qrCheckIn.setSeat(optionalBooking.get().getSeat());
                        qrCheckInRepository.save(qrCheckIn);

                        logger.info("Employee checkIn within time limit of the seat booking");
                        return constructQRCheckInSuccessResponse();
                    } else {
                        throw new BusinessException("QR_SCAN_EXPIRY",
                                "Employee is not allowed to checkIn since the time limit has been exceeded");
                    }
                } else {
                    throw new BusinessException("SEAT_NOT_BOOKED_OR_ALREADY_CHECK_IN",
                            "QR checkIn is not possible since the seat is not been booked");
                }
            } else {
                throw new BusinessException("EMP_ATTENDANCE_NOT_FOUND",
                        "There is no Employee attendance record for today");
            }
        } catch (Exception exception){
            throw new BusinessException("ERROR_QR_SCAN", exception.getMessage());
        }

    }

    public boolean isWithinCheckInBuffer(String startTime, int bufferMinutes) {

        // Parse string like "9:00" or "14:30"
        LocalTime bookingStart = LocalTime.parse(startTime, DateTimeFormatter.ofPattern("H:mm"));

        // Add buffer
        LocalTime bufferEnd = bookingStart.plusMinutes(bufferMinutes);

        // Current time
        LocalTime now = LocalTime.now();
        logger.info("Booking start Time :"+ bookingStart +" & BufferEnd Period: "+ bufferEnd + " & Current Time :"+ now);

        return !now.isBefore(bookingStart) && !now.isAfter(bufferEnd);
    }

    private QRCheckInResponseDto constructQRCheckInSuccessResponse(){
        QRCheckInResponseDto qrCheckInResponseDto = new QRCheckInResponseDto();
        qrCheckInResponseDto.setStatus("SUCCESS");
        qrCheckInResponseDto.setMessage("Employee Seat has been successfully checked In");
        qrCheckInResponseDto.setTimestamp(Instant.now());
        return qrCheckInResponseDto;
    }

    public static String getCurrentDate() {
        LocalDate today = LocalDate.now();
        return today.format(DateTimeFormatter.ISO_LOCAL_DATE); // YYYY-MM-DD
    }


}
