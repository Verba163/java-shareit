package ru.practicum.shareit.item.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemFullDto {

    Long id;
    String name;
    String description;
    Long ownerId;
    BookingDto lastBooking;
    BookingDto nextBooking;
    Set<CommentsDto> comments;
    Boolean available;
}

