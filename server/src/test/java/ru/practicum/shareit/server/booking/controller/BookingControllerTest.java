package ru.practicum.shareit.server.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.server.booking.dto.BookingDto;
import ru.practicum.shareit.server.booking.dto.BookingFullDto;
import ru.practicum.shareit.server.booking.model.enums.BookingStatus;
import ru.practicum.shareit.server.booking.service.BookingService;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private BookingController bookingController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(bookingController).build();
    }

    @Test
    public void testAddBooking() throws Exception {
        BookingDto bookingDto = new BookingDto();

        BookingFullDto bookingFullDto = new BookingFullDto();

        when(bookingService.addBooking(any(Long.class), any(BookingDto.class))).thenReturn(bookingFullDto);

        mockMvc.perform(post("/bookings")
                        .header(BookingController.X_USER_ID_HEADER, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(bookingDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(bookingFullDto.getId()));

        verify(bookingService, times(1)).addBooking(1L, bookingDto);
    }

    @Test
    public void testChangeBookingStatus() throws Exception {
        BookingFullDto bookingFullDto = new BookingFullDto();

        when(bookingService.changeBookingStatus(any(Long.class), any(Long.class), any(Boolean.class)))
                .thenReturn(bookingFullDto);

        mockMvc.perform(patch("/bookings/1")
                        .header(BookingController.X_USER_ID_HEADER, 1L)
                        .param("approved", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingFullDto.getId()));

        verify(bookingService, times(1)).changeBookingStatus(1L, 1L, true);
    }

    @Test
    public void testGetBookingByUserOrOwner() throws Exception {
        BookingFullDto bookingFullDto = new BookingFullDto();

        when(bookingService.getBookingByUserOrOwner(any(Long.class), any(Long.class)))
                .thenReturn(bookingFullDto);

        mockMvc.perform(get("/bookings/1")
                        .header(BookingController.X_USER_ID_HEADER, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingFullDto.getId()));

        verify(bookingService, times(1)).getBookingByUserOrOwner(1L, 1L);
    }

    @Test
    public void testGetAllBookingsByUser() throws Exception {
        when(bookingService.getAllBookingsByUser(any(Long.class), any(String.class)))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/bookings")
                        .header(BookingController.X_USER_ID_HEADER, 1L)
                        .param("state", "ALL"))
                .andExpect(status().isOk());

        verify(bookingService, times(1)).getAllBookingsByUser(1L, "ALL");
    }

    @Test
    public void testGetAllBookingByOwner() throws Exception {

        Long ownerId = 1L;
        String state = "ALL";
        BookingFullDto bookingFullDto = new BookingFullDto();
        bookingFullDto.setId(1L);
        bookingFullDto.setStatus(BookingStatus.APPROVED);
        when(bookingService.getAllBookingsByOwner(ownerId, state))
                .thenReturn(Collections.singletonList(bookingFullDto));


        mockMvc.perform(get("/bookings/owner")
                        .param("state", state)
                        .header(BookingController.X_USER_ID_HEADER, ownerId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].status").value("APPROVED"));

        verify(bookingService, times(1)).getAllBookingsByOwner(ownerId, state);
    }
}