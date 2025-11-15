package com.infosys.seatsync.model;

import java.util.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public class SeatAvailabilityRequest {

    @JsonProperty("wingId")
    private Long wingId;

    @JsonProperty("duration")
    private String duration; // FullDay or TimeSlot

    @JsonProperty("dates")
    private List<String> dates;

    @JsonProperty("timeSlot")
    public TimeSlot timeSlot;

    // getters / setters
    public Long getWingId() { return wingId; }
    public void setWingId(Long wingId) { this.wingId = wingId; }

    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }

    public List<String> getDates() { return dates; }
    public void setDates(List<String> dates) { this.dates = dates; } // << fixed

    public TimeSlot getTimeSlot() { return timeSlot; }
    public void setTimeSlot(TimeSlot timeSlot) { this.timeSlot = timeSlot; }

    @Override
    public int hashCode() {
        return Objects.hash(dates, duration, timeSlot, wingId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        SeatAvailabilityRequest other = (SeatAvailabilityRequest) obj;
        return Objects.equals(dates, other.dates) &&
               Objects.equals(duration, other.duration) &&
               Objects.equals(timeSlot, other.timeSlot) &&
               Objects.equals(wingId, other.wingId);
    }

    @Override
    public String toString() {
        return "SeatAvailabilityRequest [wingId=" + wingId + ", duration=" + duration + ", dates=" + dates
                + ", timeSlot=" + timeSlot + "]";
    }
}
