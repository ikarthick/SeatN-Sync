package com.infosys.seatsync.model;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BookSeatsRequest {

	    @JsonProperty("wingId")
	    private Long wingId;

	    @JsonProperty("employee_id")
	    private String employee_id; 

	    @JsonProperty("dates")
	    private List<String> dates;

		public Long getWingId() {
			return wingId;
		}

		public void setWingId(Long wingId) {
			this.wingId = wingId;
		}

		public String getEmployee_id() {
			return employee_id;
		}

		public void setEmployee_id(String employee_id) {
			this.employee_id = employee_id;
		}

		public List<String> getDates() {
			return dates;
		}

		public void setDates(List<String> dates) {
			this.dates = dates;
		}

		@Override
		public String toString() {
			return "BookASeatRequest [wingId=" + wingId + ", employee_id=" + employee_id + ", dates=" + dates + "]";
		}

		@Override
		public int hashCode() {
			return Objects.hash(dates, employee_id, wingId);
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			BookSeatsRequest other = (BookSeatsRequest) obj;
			return Objects.equals(dates, other.dates) && Objects.equals(employee_id, other.employee_id)
					&& Objects.equals(wingId, other.wingId);
		}
}
