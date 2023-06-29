package ru.practicum.shareit.booking.exception;

public class BadRequestBookingException extends RuntimeException {

    public BadRequestBookingException(String message) {
        super(message);
    }
}
