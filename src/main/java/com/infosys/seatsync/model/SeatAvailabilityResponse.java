package com.infosys.seatsync.model;

import java.util.List;
import java.util.Map;

public class SeatAvailabilityResponse {
	private Map<String, BlockAvailability> fullDayAvailability;
	private Map<String, List<String>> slotAvailability;

	private String status;
	private String message;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Map<String, BlockAvailability> getFullDayAvailability() {
		return fullDayAvailability;
	}

	public void setFullDayAvailability(Map<String, BlockAvailability> fullDayAvailability) {
		this.fullDayAvailability = fullDayAvailability;
	}

	public Map<String, List<String>> getSlotAvailability() {
		return slotAvailability;
	}

	public void setSlotAvailability(Map<String, List<String>> slotAvailability) {
		this.slotAvailability = slotAvailability;
	}
}
