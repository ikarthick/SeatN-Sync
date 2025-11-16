package com.infosys.seatsync.model;

import java.util.List;
import java.util.Map;

public class SeatAvailabilityResponse {
	private Map<String, BlockAvailability> fullDayAvailability;
	private Map<String, List<String>> slotAvailability;

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
