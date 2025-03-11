package ru.practicum.shareit.gateway.booking.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.gateway.booking.client.BookingClient;
import ru.practicum.shareit.gateway.booking.dto.BookingDto;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public final class BookingController {

    public static final String X_USER_ID_HEADER = "X-Sharer-User-Id";
    private final BookingClient bookingClient;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> addBooking(@Valid @RequestBody final BookingDto bookingDto,
                                             @RequestHeader(X_USER_ID_HEADER) Long userId) {
        log.debug("Received POST request to add a booking: {}", bookingDto);
        return bookingClient.addBooking(bookingDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> changeBookingStatus(@PathVariable Long bookingId,
                                                      @RequestHeader(X_USER_ID_HEADER) Long userId,
                                                      @RequestParam Boolean approved) {
        log.debug("Received PATCH request to change booking status for booking ID: {}", bookingId);
        return bookingClient.changeBookingStatus(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBookingByUserOrOwner(@PathVariable Long bookingId,
                                                          @RequestHeader(X_USER_ID_HEADER) Long userId) {
        return bookingClient.getBookingById(bookingId, userId);
    }

    @GetMapping
    ResponseEntity<Object> getAllBookingsByUser(@RequestParam(name = "state", defaultValue = "ALL") String state,
                                                @RequestHeader(X_USER_ID_HEADER) Long bookerId) {
        return bookingClient.getAllBookingsByUser(bookerId, state);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getAllBookingByOwner(@RequestParam(name = "state", defaultValue = "ALL") String state,
                                                       @RequestHeader(X_USER_ID_HEADER) Long ownerId) {
        return bookingClient.getAllBookingsByOwner(ownerId, state);
    }

}