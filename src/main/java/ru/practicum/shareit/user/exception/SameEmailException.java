package ru.practicum.shareit.user.exception;

public class SameEmailException extends RuntimeException {

    public SameEmailException(String message) {
        super(message);
    }
}
