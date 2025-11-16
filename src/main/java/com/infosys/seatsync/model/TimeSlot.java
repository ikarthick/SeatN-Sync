package com.infosys.seatsync.model;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TimeSlot {
	@JsonProperty("startTime")
    private String startTime;
	@JsonProperty("endTime")
    private String endTime;
    
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	@Override
	public int hashCode() {
		return Objects.hash(endTime, startTime);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TimeSlot other = (TimeSlot) obj;
		return Objects.equals(endTime, other.endTime) && Objects.equals(startTime, other.startTime);
	}
	
	@Override
	public String toString() {
		return "TimeSlot [startTime=" + startTime + ", endTime=" + endTime + "]";
	}
     
     
}
