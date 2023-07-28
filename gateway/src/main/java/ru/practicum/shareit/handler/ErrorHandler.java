package ru.practicum.shareit.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return e.getBindingResult().getFieldError().getDefaultMessage();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleWrongState(WrongStateException e) {
        return new ErrorResponse(e.getMessage());
    }
}
