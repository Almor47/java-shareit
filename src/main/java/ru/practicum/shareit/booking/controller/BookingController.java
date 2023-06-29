package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.FullBookingDto;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    private static final String HEADER = "X-Sharer-User-Id";
    private final BookingService bookingService;

    @PostMapping
    public FullBookingDto addBooking(@RequestBody BookingDto bookingDto, @RequestHeader(HEADER) Long userId) {
        return bookingService.addBooking(bookingDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public FullBookingDto updateBooking(@PathVariable Long bookingId,
                                        @RequestParam boolean approved,
                                        @RequestHeader(HEADER) Long userId) {
        return bookingService.updateBooking(bookingId, approved, userId);
    }

    @GetMapping("/{bookingId}")
    public FullBookingDto getBookingById(@PathVariable Long bookingId, @RequestHeader(HEADER) Long userId) {
        return bookingService.getBookingById(bookingId, userId);
    }

    @GetMapping
    public List<FullBookingDto> getAllUserBooking(@RequestParam(defaultValue = "ALL") String state,
                                                  @RequestHeader(HEADER) Long userId) {
        return bookingService.getAllUserBooking(state, userId);
    }

    @GetMapping("/owner")
    public List<FullBookingDto> getAllItemUserBooking(@RequestParam(defaultValue = "ALL") String state,
                                                      @RequestHeader(HEADER) Long userId) {
        return bookingService.getAllItemUserBooking(state, userId);
    }
}
