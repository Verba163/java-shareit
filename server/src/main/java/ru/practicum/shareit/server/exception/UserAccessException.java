package ru.practicum.shareit.server.exception;

public class UserAccessException extends RuntimeException {
    public UserAccessException(String message) {
        super(message);
    }
}