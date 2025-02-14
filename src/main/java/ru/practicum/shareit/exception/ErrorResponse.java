package ru.practicum.shareit.exception;

import lombok.Value;

@Value
public class ErrorResponse {
    String error;
    String description;
}