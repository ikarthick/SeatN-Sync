package com.infosys.seatsync.service;

import com.infosys.seatsync.model.SeatAvailabilityRequest;
import com.infosys.seatsync.model.SeatAvailabilityResponse;

public interface SeatsAvailabilityService {

	SeatAvailabilityResponse getAvailableSeats(SeatAvailabilityRequest request);

}
