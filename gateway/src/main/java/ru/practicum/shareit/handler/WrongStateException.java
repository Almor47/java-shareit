package ru.practicum.shareit.handler;

public class WrongStateException extends RuntimeException {

    public WrongStateException(String message) {
        super(message);

    }
}