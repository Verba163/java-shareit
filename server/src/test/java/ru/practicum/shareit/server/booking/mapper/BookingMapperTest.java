package ru.practicum.shareit.server.booking.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.practicum.shareit.server.booking.dto.BookingDto;
import ru.practicum.shareit.server.booking.dto.BookingFullDto;
import ru.practicum.shareit.server.booking.model.Booking;
import ru.practicum.shareit.server.booking.model.enums.BookingStatus;
import ru.practicum.shareit.server.item.dto.ItemDto;
import ru.practicum.shareit.server.item.mapper.ItemMapper;
import ru.practicum.shareit.server.item.model.Item;
import ru.practicum.shareit.server.user.dto.UserDto;
import ru.practicum.shareit.server.user.mapper.UserMapper;
import ru.practicum.shareit.server.user.model.User;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class BookingMapperTest {

    @InjectMocks
    private BookingMapper bookingMapper;

    @Mock
    private ItemMapper itemMapper;

    @Mock
    private UserMapper userMapper;

    private Booking booking;
    private BookingDto bookingDto;
    private BookingFullDto bookingFullDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        User booker = User.builder().id(1L).build();
        Item item = Item.builder().id(2L).build();

        booking = Booking.builder()
                .id(3L)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusDays(1))
                .item(item)
                .booker(booker)
                .status(BookingStatus.APPROVED)
                .build();

        bookingDto = BookingDto.builder()
                .id(3L)
                .start(booking.getStart())
                .end(booking.getEnd())
                .status(BookingStatus.APPROVED)
                .build();

        bookingFullDto = BookingFullDto.builder()
                .id(3L)
                .start(booking.getStart())
                .end(booking.getEnd())
                .item(itemMapper.toItemDto(item))
                .booker(userMapper.toUserDto(booker))
                .status(BookingStatus.APPROVED)
                .build();
    }

    @Test
    void toBookingDto_ShouldConvertBookingToBookingDto() {
        BookingDto result = bookingMapper.toBookingDto(booking);

        assertEquals(booking.getId(), result.getId(), "Booking ID should match");
        assertEquals(booking.getStart(), result.getStart(), "Start time should match");
        assertEquals(booking.getEnd(), result.getEnd(), "End time should match");
        assertEquals(booking.getItem().getId(), result.getItemId(), "Item ID should match");
        assertEquals(booking.getBooker().getId(), result.getBookerId(), "Booker ID should match");
        assertEquals(booking.getStatus(), result.getStatus(), "Status should match");
    }

    @Test
    void toBookingEntity_ShouldConvertBookingDtoToBookingEntity() {
        Booking result = bookingMapper.toBookingEntity(bookingDto);

        assertEquals(bookingDto.getId(), result.getId(), "BookingDto ID should match Booking ID");
        assertEquals(bookingDto.getStart(), result.getStart(), "Start time should match");
        assertEquals(bookingDto.getEnd(), result.getEnd(), "End time should match");
        assertEquals(bookingDto.getStatus(), result.getStatus(), "Status should match");
    }

    @Test
    void toBookingFullDto_ShouldConvertBookingToBookingFullDto() {

        ItemDto itemDto = new ItemDto();
        itemDto.setId(booking.getItem().getId());

        UserDto userDto = new UserDto();
        userDto.setId(booking.getBooker().getId());

        when(itemMapper.toItemDto(booking.getItem())).thenReturn(itemDto);
        when(userMapper.toUserDto(booking.getBooker())).thenReturn(userDto);

        BookingFullDto result = bookingMapper.toBookingFullDto(booking);

        assertEquals(booking.getId(), result.getId(), "Booking ID should match");
        assertEquals(booking.getStart(), result.getStart(), "Start time should match");
        assertEquals(booking.getEnd(), result.getEnd(), "End time should match");
        assertEquals(itemDto, result.getItem(), "Item DTO should match");
        assertEquals(userDto, result.getBooker(), "Booker DTO should match");
        assertEquals(booking.getStatus(), result.getStatus(), "Status should match");
    }

    @Test
    void toBookingDtoList_ShouldConvertListOfBookingsToListOfBookingFullDtos() {

        ItemDto itemDto = new ItemDto();
        itemDto.setId(booking.getItem().getId());

        UserDto userDto = new UserDto();
        userDto.setId(booking.getBooker().getId());

        when(itemMapper.toItemDto(booking.getItem())).thenReturn(itemDto);
        when(userMapper.toUserDto(booking.getBooker())).thenReturn(userDto);

        List<BookingFullDto> result = bookingMapper.toBookingDtoList(Collections.singletonList(booking));

        assertEquals(1, result.size(), "The size of the result list should be 1");
        assertEquals(booking.getId(), result.get(0).getId(), "Booking ID should match");
        assertEquals(booking.getStart(), result.get(0).getStart(), "Start time should match");
        assertEquals(booking.getEnd(), result.get(0).getEnd(), "End time should match");
        assertEquals(itemDto, result.get(0).getItem(), "Item DTO should match");
        assertEquals(userDto, result.get(0).getBooker(), "Booker DTO should match");
        assertEquals(booking.getStatus(), result.get(0).getStatus(), "Status should match");
    }
}
