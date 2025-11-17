package com.infosys.seatsync.model;

public class BlockAvailability {
	
	private Long wingId;
	private int totalSeats;
	private int availableSeats;

	private String currentStatus;

	public String getCurrentStatus() {
		return currentStatus;
	}

	public void setCurrentStatus(String currentStatus) {
		this.currentStatus = currentStatus;
	}

	public BlockAvailability() {
	}

	public BlockAvailability(Long wingId, int totalSeats, int availableSeats) {
		this.wingId = wingId;
		this.totalSeats = totalSeats;
		this.availableSeats = availableSeats;
	}

	public Long getWingId() {
		return wingId;
	}

	public void setWingId(Long wingId) {
		this.wingId = wingId;
	}

	public int getTotalSeats() {
		return totalSeats;
	}

	public void setTotalSeats(int totalSeats) {
		this.totalSeats = totalSeats;
	}

	public int getAvailableSeats() {
		return availableSeats;
	}

	public void setAvailableSeats(int availableSeats) {
		this.availableSeats = availableSeats;
	}
	
	@Override
	public String toString() {
		return "BlockAvailability [wingId=" + wingId + ", totalSeats=" + totalSeats + ", availableSeats="
				+ availableSeats + "]";
	}

}
