package ru.practicum.shareit.server.booking.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import ru.practicum.shareit.server.booking.model.enums.BookingStatus;
import ru.practicum.shareit.server.item.dto.ItemDto;
import ru.practicum.shareit.server.user.dto.UserDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class BookingFullDtoTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    public void testBookingFullDtoSerialization() throws Exception {

        ItemDto itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("Test Item");
        itemDto.setDescription("Test Description");
        itemDto.setAvailable(true);
        itemDto.setOwnerId(2L);
        itemDto.setRequestId(3L);

        UserDto booker = new UserDto();
        booker.setId(1L);
        booker.setName("Test Booker");
        booker.setEmail("test@example.com");

        BookingFullDto bookingFullDto = new BookingFullDto();
        bookingFullDto.setId(1L);
        bookingFullDto.setItem(itemDto);
        bookingFullDto.setStart(LocalDateTime.of(2025, 3, 11, 21, 20, 22));
        bookingFullDto.setEnd(LocalDateTime.of(2025, 3, 12, 21, 20, 22));
        bookingFullDto.setBooker(booker);
        bookingFullDto.setStatus(BookingStatus.APPROVED);

        String json = objectMapper.writeValueAsString(bookingFullDto);

        System.out.println(json);

        assertThat(json).contains("\"item\":{\"id\":1,\"name\":\"Test Item\",\"description\":\"Test Description\",\"ownerId\":2,\"available\":true,\"requestId\":3}");
        assertThat(json).contains("\"start\":\"2025-03-11T21:20:22\"");
        assertThat(json).contains("\"end\":\"2025-03-12T21:20:22\"");
        assertThat(json).contains("\"booker\":{\"id\":1,\"name\":\"Test Booker\",\"email\":\"test@example.com\"}");
        assertThat(json).contains("\"status\":\"APPROVED\"");
    }


    @Test
    public void testBookingFullDtoDeserialization() throws Exception {

        String json = "{\"id\":1,\"item\":{\"id\":1,\"name\":\"Test Item\",\"description\":\"Test Description\",\"available\":true},\"start\":\"2023-01-01T10:00:00\",\"end\":\"2023-01-02T10:00:00\",\"booker\":{\"id\":1,\"name\":\"Test Booker\"},\"status\":\"APPROVED\"}";

        BookingFullDto bookingFullDto = objectMapper.readValue(json, BookingFullDto.class);

        assertThat(bookingFullDto.getId()).isEqualTo(1L);
        assertThat(bookingFullDto.getItem().getId()).isEqualTo(1L);
        assertThat(bookingFullDto.getItem().getName()).isEqualTo("Test Item");
        assertThat(bookingFullDto.getItem().getDescription()).isEqualTo("Test Description");
        assertThat(bookingFullDto.getItem().getAvailable()).isTrue();
        assertThat(bookingFullDto.getStart()).isEqualTo(LocalDateTime.parse("2023-01-01T10:00:00"));
        assertThat(bookingFullDto.getEnd()).isEqualTo(LocalDateTime.parse("2023-01-02T10:00:00"));
        assertThat(bookingFullDto.getBooker().getId()).isEqualTo(1L);
        assertThat(bookingFullDto.getStatus()).isEqualTo(BookingStatus.APPROVED);
    }
}