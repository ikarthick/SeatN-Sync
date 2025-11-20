package com.infosys.seatsync.model;

import java.util.Objects;

public class AllocationResult {

	private String empId;
	private String date;
	private String allocatedSeatCode; 
	private String allocatedCubicleId;
	private boolean waitlisted;
	private String reason;
	public String getEmpId() {
		return empId;
	}
	public void setEmpId(String empId) {
		this.empId = empId;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getAllocatedSeatCode() {
		return allocatedSeatCode;
	}
	public void setAllocatedSeatCode(String allocatedSeatCode) {
		this.allocatedSeatCode = allocatedSeatCode;
	}
	public String getAllocatedCubicleId() {
		return allocatedCubicleId;
	}
	public void setAllocatedCubicleId(String allocatedCubicleId) {
		this.allocatedCubicleId = allocatedCubicleId;
	}
	public boolean isWaitlisted() {
		return waitlisted;
	}
	public void setWaitlisted(boolean waitlisted) {
		this.waitlisted = waitlisted;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	@Override
	public int hashCode() {
		return Objects.hash(allocatedCubicleId, allocatedSeatCode, date, empId, reason, waitlisted);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AllocationResult other = (AllocationResult) obj;
		return Objects.equals(allocatedCubicleId, other.allocatedCubicleId)
				&& Objects.equals(allocatedSeatCode, other.allocatedSeatCode) && Objects.equals(date, other.date)
				&& Objects.equals(empId, other.empId) && Objects.equals(reason, other.reason)
				&& waitlisted == other.waitlisted;
	}
	@Override
	public String toString() {
		return "BookASeatResponse [empId=" + empId + ", date=" + date + ", allocatedSeatCode=" + allocatedSeatCode
				+ ", allocatedCubicleId=" + allocatedCubicleId + ", waitlisted=" + waitlisted + ", reason=" + reason
				+ "]";
	}
    
}
