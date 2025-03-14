package ru.practicum.shareit.server.booking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.server.booking.dto.BookingDto;
import ru.practicum.shareit.server.booking.dto.BookingFullDto;
import ru.practicum.shareit.server.booking.mapper.BookingMapper;
import ru.practicum.shareit.server.booking.model.Booking;
import ru.practicum.shareit.server.booking.model.enums.BookingStatus;
import ru.practicum.shareit.server.booking.storage.BookingRepository;
import ru.practicum.shareit.server.exception.ItemNotAvailableException;
import ru.practicum.shareit.server.exception.NotFoundException;
import ru.practicum.shareit.server.exception.IllegalArgumentException;
import ru.practicum.shareit.server.exception.UserAccessException;
import ru.practicum.shareit.server.item.mapper.ItemMapper;
import ru.practicum.shareit.server.item.model.Item;
import ru.practicum.shareit.server.item.storage.ItemRepository;
import ru.practicum.shareit.server.user.dto.UserDto;
import ru.practicum.shareit.server.user.mapper.UserMapper;
import ru.practicum.shareit.server.user.model.User;
import ru.practicum.shareit.server.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private ItemMapper itemMapper;

    @Mock
    private BookingMapper bookingMapper;

    @InjectMocks
    private BookingService bookingService;

    private User user;
    private UserDto userDto;
    private Item item;
    private Booking booking;
    private BookingDto bookingDto;
    private BookingFullDto bookingFullDto;

    @BeforeEach
    public void setUp() {
        LocalDateTime startTime = LocalDateTime.now().plusDays(1);
        LocalDateTime endTime = LocalDateTime.now().plusDays(2);

        user = User.builder()
                .id(1L)
                .name("Test user")
                .email("Testemail@email.ru")
                .build();

        userDto = UserDto.builder()
                .id(1L)
                .name("Test user")
                .email("Testemail@email.ru")
                .build();

        item = Item.builder()
                .id(1L)
                .name("Test Item")
                .description("Test desc")
                .ownerId(1L)
                .available(true)
                .bookings(new HashSet<>())
                .comments(new HashSet<>())
                .build();

        booking = Booking.builder()
                .id(1L)
                .item(item)
                .start(startTime)
                .end(endTime)
                .status(BookingStatus.WAITING)
                .booker(user)
                .build();

        bookingDto = new BookingDto();
        bookingDto.setItemId(item.getId());
        bookingDto.setStart(startTime);
        bookingDto.setEnd(endTime);
        bookingDto.setStatus(BookingStatus.WAITING);


    }

    @Test
    public void testAddBookingSuccess() {
        Long userId = 1L;

        when(userService.getUserById(userId)).thenReturn(userDto);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        when(bookingMapper.toBookingEntity(bookingDto)).thenReturn(booking);
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        BookingFullDto expectedBookingFullDto = new BookingFullDto();
        when(bookingMapper.toBookingFullDto(booking)).thenReturn(expectedBookingFullDto);

        BookingFullDto result = bookingService.addBooking(userId, bookingDto);

        assertNotNull(result);
        verify(bookingRepository).save(any(Booking.class));
    }

    @Test
    public void testAddBookingItemNotFound() {
        Long userId = 1L;
        bookingDto.setItemId(1L);

        when(userService.getUserById(userId)).thenReturn(userDto);
        when(itemRepository.findById(1L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            bookingService.addBooking(userId, bookingDto);
        });

        assertEquals("Item with ID: 1 not found", exception.getMessage());
    }

    @Test
    public void testAddBookingInvalidTime() {
        Long userId = 1L;

        bookingDto.setStart(LocalDateTime.now().plusDays(3));
        bookingDto.setEnd(LocalDateTime.now().plusDays(2));
        booking.setStart(LocalDateTime.now().plusDays(3));
        booking.setEnd(LocalDateTime.now().plusDays(2));

        when(userService.getUserById(userId)).thenReturn(userDto);
        when(itemRepository.findById(bookingDto.getItemId())).thenReturn(Optional.of(item));
        when(bookingMapper.toBookingEntity(bookingDto)).thenReturn(booking);

        assertNotNull(booking, "Booking object should not be null");

        assertThatThrownBy(() -> bookingService.addBooking(userId, bookingDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("The end time of the reservation cannot coincide with the start of the reservation time\nor be earlier\n");
    }

    @Test
    public void testAddBookingItemNotAvailable() {
        Long userId = 1L;

        item.setAvailable(false);

        when(userService.getUserById(userId)).thenReturn(userDto);
        when(itemRepository.findById(bookingDto.getItemId())).thenReturn(Optional.of(item));
        when(bookingMapper.toBookingEntity(bookingDto)).thenReturn(booking);

        assertNotNull(booking, "Booking object should not be null");

        assertThatThrownBy(() -> bookingService.addBooking(userId, bookingDto))
                .isInstanceOf(ItemNotAvailableException.class)
                .hasMessage("Item with ID: 1 IS NOT available to order");
    }

    @Test
    public void testChangeBookingStatusUserAccessException() {
        Long bookingId = 1L;
        Long userId = 2L;
        Boolean approved = true;

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setItem(new Item());
        booking.getItem().setOwnerId(1L);
        booking.setStatus(BookingStatus.WAITING);

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        assertThatThrownBy(() -> bookingService.changeBookingStatus(bookingId, userId, approved))
                .isInstanceOf(UserAccessException.class)
                .hasMessage("You do not have access to change your reservation status");
    }

    @Test
    public void testChangeBookingStatusSuccess() {
        Long bookingId = 1L;
        Long userId = 1L;
        Boolean approved = true;

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setItem(new Item());
        booking.getItem().setOwnerId(userId);
        booking.setStatus(BookingStatus.WAITING);

        BookingFullDto bookingFullDto = new BookingFullDto();
        bookingFullDto.setStatus(BookingStatus.APPROVED);

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);
        when(bookingMapper.toBookingFullDto(booking)).thenReturn(bookingFullDto);

        BookingFullDto result = bookingService.changeBookingStatus(bookingId, userId, approved);

        assertNotNull(result, "Result should not be null");
        assertEquals(BookingStatus.APPROVED, result.getStatus());
        verify(bookingRepository).save(booking);

    }

    @Test
    public void testGetBookingByUserOrOwnerBooker() {
        Long bookingId = 1L;
        Long userId = 1L;

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setItem(new Item());
        booking.getItem().setOwnerId(userId);
        booking.setBooker(user);
        booking.setStatus(BookingStatus.WAITING);

        BookingFullDto bookingFullDto = new BookingFullDto();
        bookingFullDto.setStatus(BookingStatus.APPROVED);

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(bookingMapper.toBookingFullDto(booking)).thenReturn(bookingFullDto);

        BookingFullDto result = bookingService.getBookingByUserOrOwner(bookingId, userId);

        assertNotNull(result);
        assertEquals(bookingFullDto, result);
        verify(bookingRepository).findById(bookingId);
    }

    @Test
    public void getAllBookingsByOwnerShouldReturnCurrentBookings() {

        Long bookingId = 1L;
        Long userId = 1L;

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setItem(new Item());
        booking.getItem().setOwnerId(userId);
        booking.setBooker(user);
        booking.setStatus(BookingStatus.WAITING);

        BookingFullDto bookingFullDto = new BookingFullDto();
        bookingFullDto.setStatus(BookingStatus.APPROVED);

        when(userService.getUserById(1L)).thenReturn(userDto);
        when(bookingRepository.findBookingsByOwnerIdAndStatusCurrent(1L)).thenReturn(List.of(booking));
        when(bookingMapper.toBookingDtoList(List.of(booking))).thenReturn(List.of(bookingFullDto));

        Collection<BookingFullDto> result = bookingService.getAllBookingsByOwner(1L, "CURRENT");

        assertThat(result).hasSize(1);
        assertThat(result).contains(bookingFullDto);
    }

    @Test
    public void getAllBookingsByOwnerShouldReturnPastBookings() {
        Long bookingId = 1L;
        Long userId = 1L;

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setItem(new Item());
        booking.getItem().setOwnerId(userId);
        booking.setBooker(user);
        booking.setStatus(BookingStatus.WAITING);

        BookingFullDto bookingFullDto = new BookingFullDto();
        bookingFullDto.setStatus(BookingStatus.APPROVED);

        when(userService.getUserById(1L)).thenReturn(userDto);
        when(bookingRepository.findBookingsByOwnerIdAndStatusPast(1L)).thenReturn(List.of(booking));
        when(bookingMapper.toBookingDtoList(List.of(booking))).thenReturn(List.of(bookingFullDto));

        Collection<BookingFullDto> result = bookingService.getAllBookingsByOwner(1L, "PAST");

        assertThat(result).hasSize(1);
        assertThat(result).contains(bookingFullDto);
    }

    @Test
    public void getAllBookingsByOwnerShouldReturnFutureBookings() {

        Long bookingId = 1L;
        Long userId = 1L;

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setItem(new Item());
        booking.getItem().setOwnerId(userId);
        booking.setBooker(user);
        booking.setStatus(BookingStatus.WAITING);

        BookingFullDto bookingFullDto = new BookingFullDto();
        bookingFullDto.setStatus(BookingStatus.APPROVED);

        when(userService.getUserById(1L)).thenReturn(userDto);
        when(bookingRepository.findBookingsByOwnerIdAndStatusFuture(1L)).thenReturn(List.of(booking));
        when(bookingMapper.toBookingDtoList(List.of(booking))).thenReturn(List.of(bookingFullDto));

        Collection<BookingFullDto> result = bookingService.getAllBookingsByOwner(1L, "FUTURE");

        assertThat(result).hasSize(1);
        assertThat(result).contains(bookingFullDto);
    }

    @Test
    public void getAllBookingsByOwnerShouldReturnRejectedBookings() {

        Long bookingId = 1L;
        Long userId = 1L;

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setItem(new Item());
        booking.getItem().setOwnerId(userId);
        booking.setBooker(user);
        booking.setStatus(BookingStatus.WAITING);

        BookingFullDto bookingFullDto = new BookingFullDto();
        bookingFullDto.setStatus(BookingStatus.APPROVED);

        when(userService.getUserById(1L)).thenReturn(userDto);
        when(bookingRepository.findBookingsByOwnerIdAndStatus(1L, BookingStatus.REJECTED)).thenReturn(List.of(booking));
        when(bookingMapper.toBookingDtoList(List.of(booking))).thenReturn(List.of(bookingFullDto));

        Collection<BookingFullDto> result = bookingService.getAllBookingsByOwner(1L, "REJECTED");

        assertThat(result).hasSize(1);
        assertThat(result).contains(bookingFullDto);
    }

    @Test
    public void getAllBookingsByOwnerShouldReturnWaitingBookings() {

        Long bookingId = 1L;
        Long userId = 1L;

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setItem(new Item());
        booking.getItem().setOwnerId(userId);
        booking.setBooker(user);
        booking.setStatus(BookingStatus.WAITING);

        BookingFullDto bookingFullDto = new BookingFullDto();
        bookingFullDto.setStatus(BookingStatus.APPROVED);

        when(userService.getUserById(1L)).thenReturn(userDto);
        when(bookingRepository.findBookingsByOwnerIdAndStatus(1L, BookingStatus.WAITING)).thenReturn(List.of(booking));
        when(bookingMapper.toBookingDtoList(List.of(booking))).thenReturn(List.of(bookingFullDto));

        Collection<BookingFullDto> result = bookingService.getAllBookingsByOwner(1L, "WAITING");

        assertThat(result).hasSize(1);
        assertThat(result).contains(bookingFullDto);
    }

    @Test
    public void getAllBookingsByUserShouldReturnCurrentBookings() {

        Long bookingId = 1L;
        Long userId = 1L;

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setItem(new Item());
        booking.getItem().setOwnerId(userId);
        booking.setBooker(user);
        booking.setStatus(BookingStatus.WAITING);

        BookingFullDto bookingFullDto = new BookingFullDto();
        bookingFullDto.setStatus(BookingStatus.APPROVED);

        when(userService.getUserById(1L)).thenReturn(userDto);
        when(bookingRepository.findBookingsByBookerIdAndStatusCurrent(1L)).thenReturn(List.of(booking));
        when(bookingMapper.toBookingDtoList(List.of(booking))).thenReturn(List.of(bookingFullDto));

        Collection<BookingFullDto> result = bookingService.getAllBookingsByUser(1L, "CURRENT");

        assertThat(result).hasSize(1);
        assertThat(result).contains(bookingFullDto);
    }

    @Test
    public void getAllBookingsByUserShouldReturnPastBookings() {

        Long bookingId = 1L;
        Long userId = 1L;

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setItem(new Item());
        booking.getItem().setOwnerId(userId);
        booking.setBooker(user);
        booking.setStatus(BookingStatus.WAITING);

        BookingFullDto bookingFullDto = new BookingFullDto();
        bookingFullDto.setStatus(BookingStatus.APPROVED);

        when(userService.getUserById(1L)).thenReturn(userDto);
        when(bookingRepository.findBookingsByBookerIdAndStatusPast(1L)).thenReturn(List.of(booking));
        when(bookingMapper.toBookingDtoList(List.of(booking))).thenReturn(List.of(bookingFullDto));

        Collection<BookingFullDto> result = bookingService.getAllBookingsByUser(1L, "PAST");

        assertThat(result).hasSize(1);
        assertThat(result).contains(bookingFullDto);
    }

    @Test
    public void getAllBookingsByUserShouldReturnRejectedBookings() {

        Long bookingId = 1L;
        Long userId = 1L;

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setItem(new Item());
        booking.getItem().setOwnerId(userId);
        booking.setBooker(user);
        booking.setStatus(BookingStatus.WAITING);

        BookingFullDto bookingFullDto = new BookingFullDto();
        bookingFullDto.setStatus(BookingStatus.APPROVED);

        when(userService.getUserById(1L)).thenReturn(userDto);
        when(bookingRepository.findBookingsByBookerIdAndStatus(1L, BookingStatus.REJECTED)).thenReturn(List.of(booking));
        when(bookingMapper.toBookingDtoList(List.of(booking))).thenReturn(List.of(bookingFullDto));

        Collection<BookingFullDto> result = bookingService.getAllBookingsByUser(1L, "REJECTED");

        assertThat(result).hasSize(1);
        assertThat(result).contains(bookingFullDto);
    }

    @Test
    public void getAllBookingsByUserShouldReturnWaitingBookings() {

        Long bookingId = 1L;
        Long userId = 1L;

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setItem(new Item());
        booking.getItem().setOwnerId(userId);
        booking.setBooker(user);
        booking.setStatus(BookingStatus.WAITING);

        BookingFullDto bookingFullDto = new BookingFullDto();
        bookingFullDto.setStatus(BookingStatus.APPROVED);

        when(userService.getUserById(1L)).thenReturn(userDto);
        when(bookingRepository.findBookingsByBookerIdAndStatus(1L, BookingStatus.WAITING)).thenReturn(List.of(booking));
        when(bookingMapper.toBookingDtoList(List.of(booking))).thenReturn(List.of(bookingFullDto));

        Collection<BookingFullDto> result = bookingService.getAllBookingsByUser(1L, "WAITING");

        assertThat(result).hasSize(1);
        assertThat(result).contains(bookingFullDto);
    }

    @Test
    public void getAllBookingsByUserShouldReturnFutureBookings() {

        Long bookingId = 1L;
        Long userId = 1L;

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setItem(new Item());
        booking.getItem().setOwnerId(userId);
        booking.setBooker(user);
        booking.setStatus(BookingStatus.WAITING);

        BookingFullDto bookingFullDto = new BookingFullDto();
        bookingFullDto.setStatus(BookingStatus.APPROVED);

        when(userService.getUserById(1L)).thenReturn(userDto);
        when(bookingRepository.findBookingsByBookerIdAndStatusFuture(1L)).thenReturn(List.of(booking));
        when(bookingMapper.toBookingDtoList(List.of(booking))).thenReturn(List.of(bookingFullDto));

        Collection<BookingFullDto> result = bookingService.getAllBookingsByUser(1L, "FUTURE");

        assertThat(result).hasSize(1);
        assertThat(result).contains(bookingFullDto);
    }

}