package ru.practicum.shareit.item;

public class BadRequestItemException extends RuntimeException {
    public BadRequestItemException(String message) {
        super(message);
    }
}