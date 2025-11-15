package com.infosys.seatsync.service.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
import com.infosys.seatsync.model.SlotAvailability;
import com.infosys.seatsync.repository.SeatBookingRepository;
import com.infosys.seatsync.repository.SeatRepository;
import com.infosys.seatsync.service.SeatsAvailabilityService;

@Service
public class SeatsAvailabilityServiceImpl implements SeatsAvailabilityService {

	@Autowired
	SeatRepository seatRepository;

	@Autowired
	SeatBookingRepository bookingRepository;

	List<String> blockingSeats = List.of("BOOKED", "CHECKED_IN");

	private static final Logger logger = LoggerFactory.getLogger(SeatsAvailabilityServiceImpl.class);

	@Override
	public SeatAvailabilityResponse getAvailableSeats(SeatAvailabilityRequest request) {
		// response
		SeatAvailabilityResponse response = new SeatAvailabilityResponse();

		if (null == request || null == request.getWingId()) {
			throw new IllegalArgumentException("Request or WingId is null");
		}

		List<String> dates = request.getDates();
		if (CollectionUtils.isEmpty(dates)) {
			logger.info("date is not available, setting it as today's date");
			LocalDate today = LocalDate.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			String todayAsString = today.format(formatter);
			dates.add(todayAsString);
			logger.info("SeatAvailabilityResponse ::::" + dates);
		}

		Long wingId = request.getWingId();

		// 1. fetch all seats for wing
		List<Seat> seatList = seatRepository.getSeatsByWing(wingId);
		// total available seats for a wing
		int totalSeats = seatList == null ? 0 : seatList.size();

		Map<String, BlockAvailability> responseMap = new LinkedHashMap<>();

		// count available seats for full day
		if ("FULLDAY".equalsIgnoreCase(request.getDuration())) {

			for (String date : dates) {
				// 2.bookings for wing+date
				List<Booking> bookings = bookingRepository.findBySeatAndDate(wingId, date);

				Set<Long> blockedSeatIds = (bookings == null) ? Collections.emptySet()
						: bookings.stream().filter(b -> b.getSeat() != null && b.getSeat().getSeatId() != null)
								.filter(b -> blockingSeats.contains(b.getStatus())).map(b -> b.getSeat().getSeatId())
								.collect(Collectors.toSet());

				int available = Math.max(0, totalSeats - blockedSeatIds.size());

				BlockAvailability availability = new BlockAvailability(wingId, totalSeats, available);
				responseMap.put(date, availability);
			}
			response.setFullDayAvailability(responseMap);
		} else {
			
			//Todo time slot
		}
		
		return response;
	}

}