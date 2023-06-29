package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.FullBookingDto;

import java.util.List;

public interface BookingService {

    FullBookingDto addBooking(BookingDto bookingDto, Long userId);

    FullBookingDto updateBooking(Long bookingId, boolean approved, Long userId);

    FullBookingDto getBookingById(Long bookingId, Long userId);

    List<FullBookingDto> getAllUserBooking(String state, Long userId);

    List<FullBookingDto> getAllItemUserBooking(String state, Long userId);


}
