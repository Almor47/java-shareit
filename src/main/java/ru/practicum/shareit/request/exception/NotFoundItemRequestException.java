package ru.practicum.shareit.request.exception;

public class NotFoundItemRequestException extends RuntimeException{
    public NotFoundItemRequestException(String message) {
        super(message);
    }
}
