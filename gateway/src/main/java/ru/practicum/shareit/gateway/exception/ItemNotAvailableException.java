package ru.practicum.shareit.gateway.exception;

public class ItemNotAvailableException extends RuntimeException {
    public ItemNotAvailableException(String message) {
        super(message);
    }
}
