package ru.practicum.shareit.server.booking.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.server.booking.model.enums.BookingStatus;
import ru.practicum.shareit.server.item.dto.ItemDto;
import ru.practicum.shareit.server.user.dto.UserDto;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingFullDto {

    Long id;
    LocalDateTime start;
    LocalDateTime end;
    ItemDto item;
    UserDto booker;
    BookingStatus status;

}
