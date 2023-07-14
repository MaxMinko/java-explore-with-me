package ru.practicum.exception;

public class RepeatRequestException extends RuntimeException {
    public RepeatRequestException(String message) {
        super(message);
    }
}
