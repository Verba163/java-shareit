package ru.practicum.shareit.server.item.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import ru.practicum.shareit.server.booking.dto.BookingDto;

import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class ItemFullDtoTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testItemFullDtoSerialization() throws Exception {
        ItemFullDto itemFullDto = ItemFullDto.builder()
                .id(1L)
                .name("Item Name")
                .description("Item Description")
                .ownerId(1L)
                .requestId(1L)
                .lastBooking(new BookingDto())
                .nextBooking(new BookingDto())
                .comments(new HashSet<>())
                .available(true)
                .build();

        String json = objectMapper.writeValueAsString(itemFullDto);

        assertThat(json).contains("\"id\":1");
        assertThat(json).contains("\"name\":\"Item Name\"");
        assertThat(json).contains("\"description\":\"Item Description\"");
        assertThat(json).contains("\"available\":true");
    }

    @Test
    public void testItemFullDtoDeserialization() throws Exception {
        String json = "{\"id\":1,\"name\":\"Item Name\",\"description\":\"Item Description\",\"ownerId\":1,\"requestId\":1,\"available\":true}";

        ItemFullDto itemFullDto = objectMapper.readValue(json, ItemFullDto.class);

        assertThat(itemFullDto.getId()).isEqualTo(1L);
        assertThat(itemFullDto.getName()).isEqualTo("Item Name");
        assertThat(itemFullDto.getDescription()).isEqualTo("Item Description");
        assertThat(itemFullDto.getOwnerId()).isEqualTo(1L);
        assertThat(itemFullDto.getRequestId()).isEqualTo(1L);
        assertThat(itemFullDto.getAvailable()).isTrue();
    }
}
