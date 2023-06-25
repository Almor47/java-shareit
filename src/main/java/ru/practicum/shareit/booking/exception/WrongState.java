package ru.practicum.shareit.booking.exception;

public class WrongState extends RuntimeException {

    public WrongState(String message) {
        super(message);
    }
}
