package com.infosys.seatsync.model;

import java.util.List;
import java.util.Objects;

public class BookSeatResponse {
  
    private List<AllocationResult> results;

	public List<AllocationResult> getResults() {
		return results;
	}

	public void setResults(List<AllocationResult> results) {
		this.results = results;
	}

	@Override
	public int hashCode() {
		return Objects.hash(results);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BookSeatResponse other = (BookSeatResponse) obj;
		return Objects.equals(results, other.results);
	}

	@Override
	public String toString() {
		return "BookSeatResponse [results=" + results + "]";
	}
    
}
