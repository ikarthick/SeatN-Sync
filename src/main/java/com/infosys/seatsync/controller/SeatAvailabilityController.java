package com.infosys.seatsync.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.infosys.seatsync.model.SeatAvailabilityRequest;
import com.infosys.seatsync.model.SeatAvailabilityResponse;
import com.infosys.seatsync.service.SeatsAvailabilityService;

@RestController
@RequestMapping("/infy/seats")
public class SeatAvailabilityController {

    private final SeatsAvailabilityService seatAvailabilityService;
    private final ObjectMapper objectMapper;

    public SeatAvailabilityController(SeatsAvailabilityService seatAvailabilityService,
                                      ObjectMapper objectMapper) {
        this.seatAvailabilityService = seatAvailabilityService;
        this.objectMapper = objectMapper;
    }

    @PostMapping(value = "/availability", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAvailableSeats(@RequestBody String jsonBody,
                                               @RequestHeader(value = "Content-Type", required = false) String contentType) {
        try {
            System.out.println("JSON REQUEST -> " + jsonBody);

            SeatAvailabilityRequest request = objectMapper.readValue(jsonBody, SeatAvailabilityRequest.class);
            System.out.println("JAVA REQUEST -> " + request);

            SeatAvailabilityResponse resp = seatAvailabilityService.getAvailableSeats(request);
            return ResponseEntity.ok(null);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Invalid JSON: " + e.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server error");
        }
    }
}
