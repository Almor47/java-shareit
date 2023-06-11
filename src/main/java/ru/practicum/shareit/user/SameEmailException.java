package ru.practicum.shareit.user;

public class SameEmailException extends RuntimeException {

    public SameEmailException(String message) {
        super(message);
    }
}
