package com.infosys.seatsync.service.impl;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.infosys.seatsync.dto.CancelBookingRequestDto;
import com.infosys.seatsync.dto.ResponseDto;
import com.infosys.seatsync.entity.booking.WaitList;
import com.infosys.seatsync.entity.infra.Wing;
import com.infosys.seatsync.exception.BusinessException;
import com.infosys.seatsync.repository.*;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.infosys.seatsync.entity.booking.Booking;
import com.infosys.seatsync.entity.booking.Booking.BookingStatus;
import com.infosys.seatsync.entity.emp.Employee;
import com.infosys.seatsync.entity.infra.Seat;
import com.infosys.seatsync.model.AllocationResult;
import com.infosys.seatsync.model.BookSeatResponse;
import com.infosys.seatsync.model.BookSeatsRequest;
import com.infosys.seatsync.service.SeatBookingService;

@Service
public class SeatBookingServiceImpl implements SeatBookingService {

	@Autowired
	EmployeeRepository employeeRepository;

	@Autowired
	WaitlistRepository waitlistRepository;

	@Autowired
	SeatBookingRepository seatBookingRepository;

	@Autowired
	WingRepository wingRepository;

	@Autowired
	SeatRepository seatRepository;

	private static final Logger logger = LoggerFactory.getLogger(SeatBookingServiceImpl.class);

	private static final int CUBICLE_CAPACITY = 4;

	@Transactional
	@Override
	public BookSeatResponse bookASeat(BookSeatsRequest request) {

		try {
			BookSeatResponse response = new BookSeatResponse();

			if (null == request.getEmployee_id()) {
				throw new BusinessException("INVALID_REQUEST","Invalid Request - Emp Id is not present");
			}

			// 1.load the employee details using emp_id
			String empId = request.getEmployee_id();
			Optional<Employee> bookingEmployee = employeeRepository.findById(empId);

			if(bookingEmployee.isEmpty()) {
				throw new BusinessException("EMP_NOT_FOUND","Employee not present with Id: "+ empId);
			}

			if(request.getDates() == null || request.getDates().isEmpty())
				throw new BusinessException("DATE_VALIDATION","Booking dates cannot be empty");

			boolean allocated = false; // flag which denotes whether seat is booked or not

			// 2.get the manager id of the employee
			String managerId = bookingEmployee.get().getManager().getEmpId();
			logger.info("Manager Id of the employee ::: {}", managerId);

			// 3. preload wing seats once per call
			Long wingId = request.getWingId();
			List<Seat> wingSeats = seatRepository.getSeatsByWing(wingId);
			Map<String, List<Seat>> seatsByCubicle = wingSeats.stream().collect(Collectors.groupingBy(Seat::getCubicleId));

			/*
			 * for each date perform below steps,
			 * 1.get the bookings of the employees under
			 * same manager and in the same wing. filter by booked_by manager id,wing
			 * id,date, booking status BOOKED,CHEKED_IN
			 * 2.if booking is empty book any available seat in the same wing
			 * 3.group the bookings by cubicle_id 4.for each
			 * cubicle,
			 *     1.if size is < 4, No Seat is available. continue to check other
			 *       cubicles
			 *     2.else get the available seat and book the seat
			 *     3.In none of the
			 *       cubicles seats are not available find the nearest cubicle where his/her
			 *       teammate booked and allocate seat
			 *     4..if nearest also not available book
			 *       random.
			 *     5.if no seats are available book the employee in waiting list
			 */

			for (String date : request.getDates()) {

				allocated = false;

				AllocationResult result = new AllocationResult();
				result.setEmpId(empId);
				result.setDate(date);
				// get team mates bookings in same wing & date (booked_by = manager)
				List<Booking> teamBookings = seatBookingRepository.findBookings(managerId, date, wingId);

				if (teamBookings.isEmpty()) {
					Optional<Seat> free = findAnyFreeSeat(wingSeats, date);
					if (free.isPresent()) {
						try {
							assignSeatAndPersist(managerId, date, free.get(), result, bookingEmployee);
							allocated = true;
						} catch (Throwable e) {
							logger.error("Error occurred while trying to book seat : " + e.getMessage());
							throw new BusinessException("Error occurred while trying to book seat");
						}
					} else {
						result.setWaitlisted(true);
						result.setReason("No free seats in wing - waitlisted");
						waitingListBooking(date, bookingEmployee.get(), wingId);
					}
					response.getResults().add(result);
					continue;
				}

				// group by cubicle id for team mates
				Map<String, List<Booking>> bookingsByCubicle = teamBookings.stream()
						.filter(b -> b.getSeat() != null && b.getSeat().getCubicleId() != null)
						.collect(Collectors.groupingBy(b -> b.getSeat().getCubicleId()));

				// try cubicles where team already booked - prefer their cubicle if capacity
				// allows
				for (Map.Entry<String, List<Booking>> entry : bookingsByCubicle.entrySet()) {
					String cubicleId = entry.getKey();
					int bookedCount = entry.getValue().size();

					if (bookedCount < CUBICLE_CAPACITY) {
						// find any free seat in this cubicle
						List<Seat> cubicleSeats = seatsByCubicle.getOrDefault(cubicleId, Collections.emptyList());
						Optional<Seat> freeSeat = cubicleSeats.stream().filter(s -> isSeatFree(s, date)).findFirst();
						if (freeSeat.isPresent()) {
							assignSeatAndPersist(managerId, date, freeSeat.get(), result,
									bookingEmployee);
							allocated = true;
							break;
						}
					}
				}

				if (allocated) {
					response.getResults().add(result);
					continue;
				}

				// none of the teammate cubicles have space -> find nearest cubicle that
				// contains teammates
				Optional<String> nearestCubicleOpt = findNearestCubicleWithTeammates(bookingsByCubicle.keySet(),
						seatsByCubicle.keySet());
				if (nearestCubicleOpt.isPresent()) {
					String nearestCub = nearestCubicleOpt.get();
					List<Seat> seats = seatsByCubicle.getOrDefault(nearestCub, Collections.emptyList());
					Optional<Seat> freeSeat = seats.stream().filter(s -> isSeatFree(s, date)).findFirst();
					if (freeSeat.isPresent()) {
						assignSeatAndPersist(managerId, date, freeSeat.get(), result, bookingEmployee);
						response.getResults().add(result);
						continue;
					}
				}

				// fallback - any free seat in wing
				Optional<Seat> anyFree = findAnyFreeSeat(wingSeats, date);
				if (anyFree.isPresent()) {
					assignSeatAndPersist(managerId, date, anyFree.get(), result, bookingEmployee);
				} else {
					// STEP 7: no seats -> waitlist
					result.setWaitlisted(true);
					result.setReason("No seats available in wing - waitlisted");
					waitingListBooking(date, bookingEmployee.get(), wingId);
				}

				response.getResults().add(result);
			}

			return response;

		} catch (Exception e) {
			throw new BusinessException("SEAT_BOOKING_ERROR", "Unable to book a seat. Try Again!");
		}
	}

	@Override
	public ResponseDto cancelSeat(CancelBookingRequestDto requestDto) {

		try {
			ResponseDto responseDto = new ResponseDto();

			if(Optional.ofNullable(requestDto.getBookingId()).isPresent()){
				//check booking id is valid
				Optional<Booking> booking = seatBookingRepository.findById(requestDto.getBookingId());
				if(booking.isPresent()){
					Booking updatedBooking = booking.get();
					updatedBooking.setStatus(BookingStatus.CANCELLED);
					seatBookingRepository.save(updatedBooking);
					responseDto.setStatus("SUCCESS");
					responseDto.setMessage("Seat Booking has been successfully cancelled");
				} else {
					throw new BusinessException("INVALID_BOOKING", "Booking Id doesn't match with our records");
				}
			}

			if(Optional.ofNullable(requestDto.getWaitListId()).isPresent()){
				//check waiting list id is valid
				Optional<WaitList> waitList = waitlistRepository.findById(requestDto.getWaitListId());
				if(waitList.isPresent()){
					WaitList updatedWaitList = waitList.get();
					updatedWaitList.setStatus(WaitList.WaitlistStatus.CANCELLED);
					waitlistRepository.save(updatedWaitList);
					responseDto.setStatus("SUCCESS");
					responseDto.setMessage("Seat Booking has been successfully cancelled");
				} else {
					throw new BusinessException("INVALID_WAITLIST", "WaitList Id doesn't match with our records");
				}
			}

			//check if the seat
			return responseDto;
		} catch (Exception exception) {
			throw new BusinessException("ERROR_CANCEL_SEAT", exception.getMessage());
		}
	}

	@Transactional
	private void waitingListBooking(String bookingDate, Employee employee, Long wingId) {

		List<WaitList> waitLists = waitlistRepository.getWaitlistSorted(wingId, bookingDate);

		int priority = waitLists.isEmpty()
				? 1
				: waitLists.get(0).getPriority() + 1;

		Optional<Wing> wing = wingRepository.findById(wingId);
		if(wing.isEmpty()){
			throw new BusinessException("INVALID_WING","Wing Id is invalid");
		}

		WaitList waitList = new WaitList();
		waitList.setPriority(priority);
		waitList.setBookingDate(bookingDate);
		waitList.setEmployee(employee);
		waitList.setWing(wing.get());
		waitList.setStatus(WaitList.WaitlistStatus.WAITING);
		waitList.setRemarks("Added to waitlist");

		waitlistRepository.save(waitList);
	}

	private Optional<Seat> findAnyFreeSeat(List<Seat> wingSeats, String date) {
		return wingSeats.stream().filter(s -> isSeatFree(s, date)).findFirst();
	}

	private boolean isSeatFree(Seat seat, String date) {
		List<Booking> bookings = seatBookingRepository.findBookingInfo(seat.getSeatId(), date);
		return bookings.isEmpty();
	}

	private void assignSeatAndPersist(String managerId, String date, Seat seat, AllocationResult result, Optional<Employee> bookingEmployee) {
		result.setAllocatedSeatCode(seat.getSeatCode());
		result.setAllocatedCubicleId(seat.getCubicleId());
		result.setWaitlisted(false);
		result.setReason("Assigned seat " + seat.getSeatCode() + " in cubicle " + seat.getCubicleId());
		// persist booking
		Booking b = new Booking();
		b.setBookingDate(date);
		b.setStatus(BookingStatus.BOOKED);
		Employee emp = bookingEmployee.get();
		b.setEmployee(emp);
		b.setStartTime(getCappedTime());
		b.setEndTime("20:00");
		b.setSeat(seat);
		Employee manager = employeeRepository.findById(managerId).orElse(null);
		b.setBookedBy(manager);
		seatBookingRepository.save(b);
	}

	public static String getCappedTime() {

		// Formatter: HH:mm (not HH:MM — MM is for month)
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

		// Current time
		LocalTime now = LocalTime.now();

		// Add 30 minutes
		LocalTime updated = now.plusMinutes(30);

		// Cap at 12:00 PM
		LocalTime cap = LocalTime.of(12, 0);
		if (updated.isAfter(cap)) {
			updated = cap;
		}

		// Return formatted result
		return updated.format(formatter);
	}

	private Optional<String> findNearestCubicleWithTeammates(Set<String> bookedCubicles, Set<String> allCubicles) {
		if (bookedCubicles.isEmpty() || allCubicles.isEmpty())
			return Optional.empty();
		// try numeric heuristic
		Map<String, Integer> mapBooked = bookedCubicles.stream()
				.collect(Collectors.toMap(c -> c, this::extractNum, (a, b) -> a));
		Map<String, Integer> mapAll = allCubicles.stream()
				.collect(Collectors.toMap(c -> c, this::extractNum, (a, b) -> a));

		List<Integer> allNums = mapAll.values().stream().filter(Objects::nonNull).toList();
		if (mapBooked.values().stream().allMatch(Objects::isNull) || allNums.isEmpty()) {
			return Optional.of(bookedCubicles.iterator().next());
		}

		// pick booked cubicle with the smallest average distance to all cubicles
		String bestCub = null;
		int bestScore = Integer.MAX_VALUE;
		for (Map.Entry<String, Integer> e : mapBooked.entrySet()) {
			Integer vk = e.getValue();
			if (vk == null)
				continue;
			int score = allNums.stream().mapToInt(n -> Math.abs(n - vk)).sum();
			if (score < bestScore) {
				bestScore = score;
				bestCub = e.getKey();
			}
		}
		return Optional.ofNullable(bestCub == null ? bookedCubicles.iterator().next() : bestCub);
	}

	private Integer extractNum(String cubicleId) {
		if (cubicleId == null)
			return null;
		String digits = cubicleId.replaceAll("\\D+", "");
		if (digits.isEmpty())
			return null;
		try {
			return Integer.valueOf(digits);
		} catch (NumberFormatException ex) {
			return null;
		}
	}
}
