package com.infosys.seatsync.scheduler;

import com.infosys.seatsync.entity.booking.Booking;
import com.infosys.seatsync.entity.booking.QRCheckIn;
import com.infosys.seatsync.entity.booking.WaitList;
import com.infosys.seatsync.repository.QRCheckInRepository;
import com.infosys.seatsync.repository.SeatBookingRepository;
import com.infosys.seatsync.repository.WaitlistRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Component
public class SeatAutoReleaseScheduler {

    @Autowired
    SeatBookingRepository seatBookingRepository;

    @Autowired
    QRCheckInRepository qrCheckInRepository;

    @Autowired
    WaitlistRepository waitlistRepository;

    private static final Logger logger = LoggerFactory.getLogger(SeatAutoReleaseScheduler.class);

    @Transactional
    // Scheduler runs every 5 mins from 8 AM to 12 PM
    @Scheduled(cron = "0 */5 8-11 * * *")
    public void autoReleaseExpiredBookings() {
        logger.info("Running auto release...");
        LocalDate today = LocalDate.now();
        String todayStr = today.toString();

        List<Booking> bookings = seatBookingRepository.findTodayBooked(todayStr);

        LocalDateTime now = LocalDateTime.now();

        for (Booking booking : bookings) {

            // 1. Convert date + startTime to LocalDateTime
            LocalDate date = LocalDate.parse(booking.getBookingDate());
            LocalTime start = LocalTime.parse(booking.getStartTime());
            LocalDateTime startDateTime = LocalDateTime.of(date, start);

            // 2. Add buffer of 45 mins
            LocalDateTime bufferEnd = startDateTime.plusMinutes(45);

            // 3. Check if current time > buffer time
            if (now.isAfter(bufferEnd)) {

                // 4. Check if check-in exists
                QRCheckIn checkIn = qrCheckInRepository.findByBookingId(booking.getBookingId());

                if (checkIn == null) {
                    booking.setStatus(Booking.BookingStatus.AUTO_RELEASED);
                    seatBookingRepository.save(booking);

                    logger.info("AUTO-RELEASED | Booking ID: {} | Employee: {}",
                            booking.getBookingId(),
                            booking.getEmployee().getEmpId());
                    processAutoReleaseAndWaitList(booking);
                }
            }
        }
    }

    private void processAutoReleaseAndWaitList(Booking booking) {
        try {
            Long wingId = booking.getSeat().getWing().getWingId();

            // 1. Fetch next person in waiting list for this wing
            Optional<WaitList> nextWait = waitlistRepository
                    .findTopByWing_WingIdAndStatusOrderByPriorityAsc(
                            wingId, WaitList.WaitlistStatus.WAITING
                    );

            if (nextWait.isPresent()) {

                WaitList entry = nextWait.get();

                logger.info("WAITLIST MATCH FOUND | Wing: {} | Employee: {}",
                        wingId, entry.getEmployee().getEmpId());

                // 2. Create new booking for waitlist employee
                Booking newBooking = new Booking();
                newBooking.setEmployee(entry.getEmployee());
                newBooking.setSeat(booking.getSeat());
                newBooking.setBookingDate(booking.getBookingDate());
                newBooking.setStartTime(booking.getStartTime());
                newBooking.setEndTime(booking.getEndTime());
                newBooking.setStatus(Booking.BookingStatus.ASSIGNED_FROM_WAITLIST);

                seatBookingRepository.save(newBooking);

                // 3. Update waitlist status
                entry.setStatus(WaitList.WaitlistStatus.CONFIRMED);
                entry.setRemarks("Auto-allocated seat " + booking.getSeat().getSeatCode());
                waitlistRepository.save(entry);

                logger.info("WAITLIST ALLOCATED | WaitlistID: {} | New Booking: {}",
                        entry.getId(), newBooking.getBookingId());

                // 4. Reorder priorities for remaining waiting users in same wing
                List<WaitList> remaining = waitlistRepository
                        .findByWing_WingIdAndStatusOrderByPriorityAsc(wingId, WaitList.WaitlistStatus.WAITING);

                int priority = 1;
                for (WaitList w : remaining) {
                    w.setPriority(priority++);
                }
                waitlistRepository.saveAll(remaining);

                logger.info("WAITLIST PRIORITIES UPDATED | Wing: {}", wingId);
            }

        } catch (Exception ex) {
            logger.error("Error handling waitlist: {}", ex.getMessage(), ex);
        }
    }
}

