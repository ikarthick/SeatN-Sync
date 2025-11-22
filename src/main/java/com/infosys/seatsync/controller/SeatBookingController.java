package com.infosys.seatsync.controller;

import com.infosys.seatsync.dto.AttendanceResponseDto;
import com.infosys.seatsync.dto.CancelBookingRequestDto;
import com.infosys.seatsync.dto.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.infosys.seatsync.model.BookSeatsRequest;
import com.infosys.seatsync.model.BookSeatResponse;
import com.infosys.seatsync.model.SeatAvailabilityRequest;
import com.infosys.seatsync.model.SeatAvailabilityResponse;
import com.infosys.seatsync.service.SeatBookingService;

@RestController
@RequestMapping("/infy/seats")
public class SeatBookingController {

	private final SeatBookingService seatBookingService;
	private final ObjectMapper objectMapper;

	public SeatBookingController(SeatBookingService seatBookingService,ObjectMapper objectMapper) {
        this.seatBookingService = seatBookingService;
        this.objectMapper = objectMapper;
    }

	@PostMapping(value = "/bookSeat", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getAvailableSeats(@RequestBody String jsonBody,
			@RequestHeader(value = "Content-Type", required = false) String contentType) {
		try {
			System.out.println("JSON REQUEST -> " + jsonBody);

            BookSeatsRequest request = objectMapper.readValue(jsonBody, BookSeatsRequest.class);
			System.out.println("JAVA REQUEST -> " + request);

			BookSeatResponse resp = seatBookingService.bookASeat(request);
			return ResponseEntity.ok(resp);

		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body("Invalid JSON: " + e.getMessage());
		} catch (Exception ex) {
			ex.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server error");
		}
	}

	@PostMapping(value = "/cancel")
	public ResponseEntity<ResponseDto> cancelSeat(@RequestBody CancelBookingRequestDto cancelBookingRequestDto){
		return new ResponseEntity<>(seatBookingService.cancelSeat(cancelBookingRequestDto), HttpStatus.OK);
	}
}
