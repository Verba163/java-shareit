package ru.practicum.shareit.server.booking.service;

import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
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
import ru.practicum.shareit.server.item.model.Item;
import ru.practicum.shareit.server.item.storage.ItemRepository;
import ru.practicum.shareit.server.user.mapper.UserMapper;
import ru.practicum.shareit.server.user.model.User;
import ru.practicum.shareit.server.user.service.UserService;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final UserMapper userMapper;
    private final UserService userService;
    private final ItemRepository itemRepository;

    @Transactional
    public BookingFullDto addBooking(Long userId, BookingDto bookingDto) {

        User user = userMapper.toUserEntity(userService.getUserById(userId));

        Item item = itemRepository.findById(bookingDto.getItemId()).orElseThrow(() ->
                new NotFoundException(String.format("Item with ID: %d not found", bookingDto.getItemId())));

        Booking booking = bookingMapper.toBookingEntity(bookingDto);
        booking.setBooker(user);
        booking.setItem(item);
        booking.setStatus(BookingStatus.WAITING);

        if (booking.getStart().equals(booking.getEnd()) || booking.getEnd().isBefore(booking.getStart())) {
            throw new IllegalArgumentException("""
                    The end time of the reservation cannot coincide with the start of the reservation time
                    or be earlier
                    """);
        }

        if (!item.getAvailable()) {
            throw new ItemNotAvailableException(String.format("Item with ID: %d IS NOT available to order", item.getId()));
        }

        item.getBookings().add(booking);

        log.info("The booking has been successfully assigned some parameters");

        return bookingMapper.toBookingFullDto(bookingRepository.save(booking));

    }

    @Transactional
    public BookingFullDto changeBookingStatus(Long bookingId, Long userId, Boolean approved) {

        Booking booking = bookingRepository.findById(bookingId).orElseThrow(()
                -> new NotFoundException(String.format("Booking with id %d not found", bookingId)));

        if (!booking.getItem().getOwnerId().equals(userId)) {
            throw new UserAccessException("You do not have access to change your reservation status");
        }

        booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        Booking updatedBooking = bookingRepository.save(booking);
        return bookingMapper.toBookingFullDto(updatedBooking);
    }

    @Transactional(readOnly = true)
    public Collection<BookingFullDto> getAllBookingsByUser(Long bookerId, String state) {
        userService.getUserById(bookerId);

        List<Booking> bookings = switch (state.toUpperCase()) {
            case "CURRENT" -> bookingRepository.findBookingsByBookerIdAndStatusCurrent(bookerId);
            case "PAST" -> bookingRepository.findBookingsByBookerIdAndStatusPast(bookerId);
            case "FUTURE" -> bookingRepository.findBookingsByBookerIdAndStatusFuture(bookerId);
            case "WAITING" -> bookingRepository.findBookingsByBookerIdAndStatus(bookerId, BookingStatus.WAITING);
            case "REJECTED" -> bookingRepository.findBookingsByBookerIdAndStatus(bookerId, BookingStatus.REJECTED);
            default -> bookingRepository.findBookingsByBookerId(bookerId);
        };

        return bookingMapper.toBookingDtoList(bookings)
                .stream()
                .sorted(Comparator.comparing(BookingFullDto::getStart).reversed())
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Collection<BookingFullDto> getAllBookingsByOwner(Long ownerId, String state) {
        userService.getUserById(ownerId);

        List<Booking> bookings = switch (state.toUpperCase()) {
            case "CURRENT" -> bookingRepository.findBookingsByOwnerIdAndStatusCurrent(ownerId);
            case "PAST" -> bookingRepository.findBookingsByOwnerIdAndStatusPast(ownerId);
            case "FUTURE" -> bookingRepository.findBookingsByOwnerIdAndStatusFuture(ownerId);
            case "WAITING" -> bookingRepository.findBookingsByOwnerIdAndStatus(ownerId, BookingStatus.WAITING);
            case "REJECTED" -> bookingRepository.findBookingsByOwnerIdAndStatus(ownerId, BookingStatus.REJECTED);
            default -> bookingRepository.findBookingsByOwnerId(ownerId);
        };

        return bookingMapper.toBookingDtoList(bookings)
                .stream()
                .sorted(Comparator.comparing(BookingFullDto::getStart).reversed())
                .collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public BookingFullDto getBookingByUserOrOwner(Long bookingId, Long userId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException(String.format("Booking with id %d not found", bookingId)));

        boolean isBooker = booking.getBooker().getId().equals(userId);
        boolean isOwner = booking.getItem().getOwnerId().equals(userId);

        if (!isBooker && !isOwner) {
            throw new UserAccessException("You do not have access: You are not the booker or the owner");
        }

        return bookingMapper.toBookingFullDto(booking);
    }

}
