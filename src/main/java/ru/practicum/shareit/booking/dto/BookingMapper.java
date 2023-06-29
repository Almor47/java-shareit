package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.enumerated.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.io.Serializable;

public class BookingMapper implements Serializable {

    public static Booking mapToBooking(BookingDto bookingDto, User user) {
        Booking booking = new Booking();
        booking.setStart(bookingDto.getStart());
        booking.setEnd(bookingDto.getEnd());
        booking.setItemId(bookingDto.getItemId());
        booking.setBookerId(user.getId());
        booking.setStatus(Status.WAITING);
        return booking;
    }

    public static FullBookingDto mapToFullBooking(Booking booking, User user, Item item) {
        FullBookingDto fullBookingDto = new FullBookingDto();
        fullBookingDto.setId(booking.getId());
        fullBookingDto.setStart(booking.getStart());
        fullBookingDto.setEnd(booking.getEnd());
        fullBookingDto.setItem(item);
        fullBookingDto.setBooker(user);
        fullBookingDto.setStatus(booking.getStatus());
        return fullBookingDto;
    }
}
