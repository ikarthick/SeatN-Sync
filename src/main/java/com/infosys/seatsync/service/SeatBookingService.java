package com.infosys.seatsync.service;

import com.infosys.seatsync.dto.CancelBookingRequestDto;
import com.infosys.seatsync.dto.ResponseDto;
import com.infosys.seatsync.model.BookSeatsRequest;
import com.infosys.seatsync.model.BookSeatResponse;

public interface SeatBookingService {

	BookSeatResponse bookASeat(BookSeatsRequest request);

	ResponseDto cancelSeat(CancelBookingRequestDto requestDto);

}
