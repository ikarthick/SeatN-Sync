package com.infosys.seatsync.dto;

public class CancelBookingRequestDto {
    private Long bookingId;
    private Long waitListId;

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public Long getWaitListId() {
        return waitListId;
    }

    public void setWaitListId(Long waitListId) {
        this.waitListId = waitListId;
    }
}
