package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingFullDto;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.Collection;

/**
 * TODO Sprint add-bookings.
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(path = "/bookings")
public final class BookingController {

    public static final String X_USER_ID_HEADER = "X-Sharer-User-Id";
    private final BookingService bookingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingFullDto addBooking(@Valid @RequestBody final BookingDto bookingDto,
                                     @RequestHeader(X_USER_ID_HEADER) Long userId) {
        log.debug("Received POST request to add a booking: {}", bookingDto);
        return bookingService.addBooking(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingFullDto changeBookingStatus(@PathVariable Long bookingId,
                                              @RequestHeader(X_USER_ID_HEADER) Long userId,
                                              @RequestParam Boolean approved) {
        log.debug("Received PATCH request to patch a booking: {}", bookingId);
        return bookingService.changeBookingStatus(bookingId, userId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingFullDto getBookingByUserOrOwner(@PathVariable Long bookingId,
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