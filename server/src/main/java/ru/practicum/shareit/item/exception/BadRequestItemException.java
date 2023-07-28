package ru.practicum.shareit.item.exception;

public class BadRequestItemException extends RuntimeException {
    public BadRequestItemException(String message) {
        super(message);
    }
}