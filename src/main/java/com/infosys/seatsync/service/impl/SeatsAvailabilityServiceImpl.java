package com.infosys.seatsync.service.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import com.infosys.seatsync.entity.booking.WaitList;
import com.infosys.seatsync.exception.BusinessException;
import com.infosys.seatsync.repository.WaitlistRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.infosys.seatsync.entity.booking.Booking;
import com.infosys.seatsync.entity.infra.Seat;
import com.infosys.seatsync.model.BlockAvailability;
import com.infosys.seatsync.model.SeatAvailabilityRequest;
import com.infosys.seatsync.model.SeatAvailabilityResponse;
import com.infosys.seatsync.repository.SeatBookingRepository;
import com.infosys.seatsync.repository.SeatRepository;
import com.infosys.seatsync.service.SeatsAvailabilityService;
import com.infosys.seatsync.service.util.SlotUtil;

@Service
public class SeatsAvailabilityServiceImpl implements SeatsAvailabilityService {

	@Autowired
	SeatRepository seatRepository;

	@Autowired
	SeatBookingRepository bookingRepository;

	@Autowired
	WaitlistRepository waitlistRepository;

	List<String> blockingSeats = List.of("BOOKED", "CHECKED_IN");

	private static final Logger logger = LoggerFactory.getLogger(SeatsAvailabilityServiceImpl.class);

	@Override
	public SeatAvailabilityResponse getAvailableSeats(SeatAvailabilityRequest request) {

		try {
			SeatAvailabilityResponse response = new SeatAvailabilityResponse();

			if (request == null || request.getWingId() == null) {
				throw new BusinessException("INVALID_REQUEST","Request or WingId is null");
			}

			List<String> dates = request.getDates();
			if (CollectionUtils.isEmpty(dates)) {
				LocalDate today = LocalDate.now();
				dates.add(today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
			}

			logger.info("SeatAvailabilityResponse :::: Dates" + dates);

			Long wingId = request.getWingId();

			// Fetch seats for wing
			List<Seat> seats = seatRepository.getSeatsByWing(wingId);

			if(seats.isEmpty()){
				response.setMessage("Seats were not been added to specific wing Id:"+ wingId);
				response.setStatus("NO_SEATS_ADDED");
				return response;
			}

			logger.info("SeatAvailabilityResponse :::: seats" + seats);

			if (request.getDuration().equalsIgnoreCase("FullDay")) {
				response.setFullDayAvailability(processFullDay(request, seats));
			} else {
				response.setSlotAvailability(processSlots(request, seats));
			}

			logger.info("SeatAvailabilityResponse :::: Resonse ::" + response);

			return response;
		} catch (Exception exception){
			throw new BusinessException("AVAILABILITY_ERROR", "Unable to fetch the available seats. Try Again!");
		}
	}

	private Map<String, BlockAvailability> processFullDay(SeatAvailabilityRequest req, List<Seat> seats) {
		Map<String, BlockAvailability> map = new HashMap<String, BlockAvailability>();

		for (String date : req.getDates()) {
			int available = 0;

			for (Seat seat : seats) {
				// Fetch bookings with the status Booked and CheckedIn
				List<Booking> bookings = bookingRepository.findBookingInfo(seat.getSeatId(), date);
				logger.info("processFullDay bookings:::  " + bookings);
				// if bookings is empty, then no bookings is available for that seat for that
				// day, So increase available seats count.
				if (bookings.isEmpty()) {
					available++;
				}
			}
			BlockAvailability ba = new BlockAvailability();
			ba.setWingId(req.getWingId());
			ba.setTotalSeats(seats.size());
			ba.setAvailableSeats(available);

			// -----------------------
			// 1️⃣ CHECK USER EXISTING BOOKING
			// -----------------------
			List<Booking> userBookings =
					bookingRepository.findEmployeeBookings(req.getEmployeeId(), date);

			Optional<WaitList> waitList = waitlistRepository.findByEmployee_EmpIdAndBookingDateAndStatus(req.employeeId, date, WaitList.WaitlistStatus.WAITING.toString());

			if (!userBookings.isEmpty() || waitList.isPresent()) {
				// User already has a booking on this date
				ba.setAvailableSeats(0);
				ba.setCurrentStatus("ALREADY_BOOKED");
				map.put(date, ba);
				continue; // <-- Skip other logic
			}

			// -----------------------
			//  ADD CURRENT STATUS LOGIC
			// -----------------------

			if (available > 0) {
				// Seats available → No WL status required
				ba.setCurrentStatus(null);

			} else {

				// check waitlist for this wing
				Optional<WaitList> nextWait = waitlistRepository
						.findTopByWing_WingIdAndStatusOrderByPriorityAsc(
								req.getWingId(), WaitList.WaitlistStatus.WAITING);

				if (nextWait.isPresent()) {
					// next priority = smallest priority in table
					ba.setCurrentStatus("WL-" + nextWait.get().getPriority());
				} else {
					// no one in WL → WL-0 (basically new WL entry will become 1)
					ba.setCurrentStatus("WL-0");
				}
			}

			map.put(date, ba);
			logger.info("processFullDay :::  " + map.toString());
		}
		return map;
	}

	private Map<String, List<String>> processSlots(SeatAvailabilityRequest req, List<Seat> seats) {
		Map<String, List<String>> map = new HashMap<>();
   
		String reqStart = req.getTimeSlot().getStartTime();
		String reqEnd = req.getTimeSlot().getEndTime();

		for (String date : req.getDates()) {

			Set<String> freeSlots = new HashSet<>(SlotUtil.getSlots().keySet());

			for (Seat seat : seats) {
				List<Booking> bookings = bookingRepository.findBookingInfo(seat.getSeatId(), date);
				logger.info("processSlots bookings:::  " + bookings);
				for (Booking b : bookings) {

					// Full day booked → no slots allowed
					if (SlotUtil.isFullDayBooking(b.getStartTime(), b.getEndTime())) {
						freeSlots.clear();
						break;
					}

					// If overlaps remove slot
					if (SlotUtil.overlaps(reqStart, reqEnd, b.getStartTime(), b.getEndTime())) {
						String s = SlotUtil.getSlotName(b.getStartTime(), b.getEndTime());
						freeSlots.remove(s);
					}
				}
			}

			map.put(date, new ArrayList<>(freeSlots));
			logger.info("processSlots :::  " + map);

		}

		return map;
	}

}
