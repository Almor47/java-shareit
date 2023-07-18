package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.handler.WrongStateException;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
@RequestMapping(path = "/bookings")
public class BookingController {

	private final BookingClient bookingClient;
	private static final String HEADER = "X-Sharer-User-Id";

	@GetMapping
	public ResponseEntity<Object> getAllUserBooking(
			@RequestParam(name = "state", defaultValue = "ALL") String state,
			@RequestHeader(HEADER) long userId,
			@PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
			@Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
		BookingState stateCurrent = BookingState.from(state)
				.orElseThrow(() -> new WrongStateException("Unknown state: " + state));
		log.info("Get booking with state {}, userId={}, from={}, size={}", state, userId, from, size);
		return bookingClient.getAllUserBooking(state, userId, from, size);
	}


	@PostMapping
	public ResponseEntity<Object> addBooking(@RequestBody @Valid BookingDto bookingDto, @RequestHeader(HEADER) long userId) {
		log.info("Creating booking {}, userId={}", bookingDto, userId);
		return bookingClient.addBooking(bookingDto, userId);
	}


	@GetMapping("/{bookingId}")
	public ResponseEntity<Object> getBookingById(@PathVariable long bookingId, @RequestHeader(HEADER) long userId) {
		log.info("Get booking by bookingId={}, userId={}", bookingId, userId);
		return bookingClient.getBookingById(bookingId, userId);
	}

	@GetMapping("/owner")
	public ResponseEntity<Object> getAllItemUserBooking(
			@RequestParam(defaultValue = "ALL") String state,
			@RequestHeader(HEADER) Long userId,
			@PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
			@Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
		BookingState stateCurrent = BookingState.from(state)
				.orElseThrow(() -> new WrongStateException("Unknown state: " + state));
		log.info("Get booking with state {}, userId={}, from={}, size={}", state, userId, from, size);
		return bookingClient.getAllItemUserBooking(state, userId, from, size);
	}

	@PatchMapping("/{bookingId}")
	public ResponseEntity<Object> updateBooking(@PathVariable long bookingId,
												@RequestParam boolean approved, @RequestHeader(HEADER) long userId) {
		return bookingClient.updateBooking(bookingId, approved, userId);
	}

}
