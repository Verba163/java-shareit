package ru.practicum.shareit.gateway.booking.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.gateway.booking.status.BookingStatus;
import ru.practicum.shareit.gateway.item.dto.ItemDto;
import ru.practicum.shareit.gateway.user.dto.UserDto;

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
