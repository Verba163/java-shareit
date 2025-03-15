package ru.practicum.shareit.server.booking.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.server.booking.model.enums.BookingStatus;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingDto {
    Long id;

    @Future(message = "Start time can not be in past")
    @NotNull(message = "Start time must be provided.")
    LocalDateTime start;

    @Future(message = "End time can not be in past")
    @NotNull(message = "End time must be provided.")
    LocalDateTime end;

    Long itemId;
    Long bookerId;
    BookingStatus status;
}