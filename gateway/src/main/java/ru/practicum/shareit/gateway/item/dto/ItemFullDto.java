package ru.practicum.shareit.gateway.item.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.gateway.booking.dto.BookingDto;

import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemFullDto {

    Long id;

    @NotBlank(message = "Not able to add item with no name")
    String name;

    @NotBlank(message = "Not able to add item with no description")
    String description;

    Long ownerId;
    Long requestId;
    BookingDto lastBooking;
    BookingDto nextBooking;
    Set<CommentsDto> comments;

    @NotBlank(message = "Not able to add item with no available")
    Boolean available;
}

