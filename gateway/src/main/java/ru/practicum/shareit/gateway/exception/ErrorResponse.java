package ru.practicum.shareit.gateway.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ErrorResponse {
    String error;
    String description;
}