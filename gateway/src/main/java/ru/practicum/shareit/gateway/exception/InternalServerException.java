package ru.practicum.shareit.gateway.exception;

public class InternalServerException extends RuntimeException {
    public InternalServerException(String message) {
        super(message);
    }
}