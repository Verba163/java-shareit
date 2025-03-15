package ru.practicum.shareit.server.item.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;

import java.util.HashSet;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JsonTest
public class ItemTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testItemSerialization() throws Exception {
        Item item = Item.builder()
                .id(1L)
                .name("Item Name")
                .description("Item Description")
                .ownerId(1L)
                .available(true)
                .comments(new HashSet<>())
                .bookings(new HashSet<>())
                .build();

        String json = objectMapper.writeValueAsString(item);

        assertThat(json).contains("\"id\":1");
        assertThat(json).contains("\"name\":\"Item Name\"");
        assertThat(json).contains("\"description\":\"Item Description\"");
        assertThat(json).contains("\"available\":true");
    }

    @Test
    public void testItemDeserialization() throws Exception {
        String json = "{\"id\":1,\"name\":\"Item Name\",\"description\":\"Item Description\",\"ownerId\":1,\"available\":true}";

        Item item = objectMapper.readValue(json, Item.class);

        assertThat(item.getId()).isEqualTo(1L);
        assertThat(item.getName()).isEqualTo("Item Name");
        assertThat(item.getDescription()).isEqualTo("Item Description");
        assertThat(item.getOwnerId()).isEqualTo(1L);
        assertThat(item.getAvailable()).isTrue();
    }
}