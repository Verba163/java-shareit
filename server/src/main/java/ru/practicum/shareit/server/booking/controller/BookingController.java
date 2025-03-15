package ru.practicum.shareit.server.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.server.booking.dto.BookingDto;
import ru.practicum.shareit.server.booking.dto.BookingFullDto;
import ru.practicum.shareit.server.booking.service.BookingService;

import java.util.Collection;

/**
 * TODO Sprint add-bookings.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public final class BookingController {

    public static final String X_USER_ID_HEADER = "X-Sharer-User-Id";
    public static final String BOOKING_ID_PATH = "/{booking-Id}";
    public static final String BOOKING_ID = "booking-Id";
    private final BookingService bookingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingFullDto addBooking(@RequestBody final BookingDto bookingDto,
                                     @RequestHeader(X_USER_ID_HEADER) Long userId) {
        log.debug("Received POST request to add a booking: {}", bookingDto);
        return bookingService.addBooking(userId, bookingDto);
    }

    @PatchMapping(BOOKING_ID_PATH)
    public BookingFullDto changeBookingStatus(@PathVariable(BOOKING_ID) Long bookingId,
                                              @RequestHeader(X_USER_ID_HEADER) Long userId,
                                              @RequestParam Boolean approved) {
        log.debug("Received PATCH request to patch a booking: {}", bookingId);
        return bookingService.changeBookingStatus(userId, bookingId, approved);
    }

    @GetMapping(BOOKING_ID_PATH)
    public BookingFullDto getBookingByUserOrOwner(@PathVariable(BOOKING_ID) Long bookingId,
                                                  @RequestHeader(X_USER_ID_HEADER) Long userId) {

        return bookingService.getBookingByUserOrOwner(bookingId, userId);
    }

    @GetMapping
    public Collection<BookingFullDto> getAllBookingsByUser(@RequestParam(name = "state", defaultValue = "ALL") String state,
                                                           @RequestHeader(X_USER_ID_HEADER) Long bookerId) {
        return bookingService.getAllBookingsByUser(bookerId, state);
    }

    @GetMapping("/owner")
    public Collection<BookingFullDto> getAllBookingByOwner(@RequestParam(name = "state", defaultValue = "ALL") String state,
                                                           @RequestHeader(X_USER_ID_HEADER) Long ownerId) {
        return bookingService.getAllBookingsByOwner(ownerId, state);
    }

}