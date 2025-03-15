package ru.practicum.shareit.gateway.exception;

public class UserAccessException extends RuntimeException {
    public UserAccessException(String message) {
        super(message);
    }
}