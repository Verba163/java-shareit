package ru.practicum.shareit.server.item.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.server.item.dto.ItemDto;
import ru.practicum.shareit.server.item.dto.ItemFullDto;
import ru.practicum.shareit.server.item.model.Item;
import ru.practicum.shareit.server.request.model.ItemRequest;
import ru.practicum.shareit.server.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ItemMapperTest {

    private ItemMapper itemMapper;
    private ItemRequest itemRequest;
    private User user;

    @BeforeEach
    void setUp() {
        itemMapper = new ItemMapper();
        itemRequest = ItemRequest.builder()
                .id(1L)
                .description("Text")
                .requester(user)
                .created(LocalDateTime.now())
                .build();

    }

    @Test
    void toItemDto_ShouldConvertItemToItemDto() {

        Item item = Item.builder()
                .id(1L)
                .name("Test Item")
                .description("Item Description")
                .ownerId(1L)
                .available(true)
                .request(itemRequest)
                .build();

        ItemDto itemDto = itemMapper.toItemDto(item);

        assertEquals(item.getId(), itemDto.getId(), "Item ID should match");
        assertEquals(item.getName(), itemDto.getName(), "Item name should match");
        assertEquals(item.getDescription(), itemDto.getDescription(), "Item description should match");
        assertEquals(item.getOwnerId(), itemDto.getOwnerId(), "Item ownerId should match");
        assertEquals(item.getAvailable(), itemDto.getAvailable(), "Item availability should match");
        assertEquals(item.getRequest().getId(), itemDto.getRequestId(), "Item request ID should match");
    }

    @Test
    void toItemFullDto_ShouldConvertItemToItemFullDto() {

        Item item = Item.builder()
                .id(1L)
                .name("Test Item")
                .description("Item Description")
                .ownerId(1L)
                .available(true)
                .build();

        ItemFullDto itemFullDto = itemMapper.toItemFullDto(item);

        assertEquals(item.getId(), itemFullDto.getId(), "Item ID should match");
        assertEquals(item.getName(), itemFullDto.getName(), "Item name should match");
        assertEquals(item.getDescription(), itemFullDto.getDescription(), "Item description should match");
        assertEquals(item.getOwnerId(), itemFullDto.getOwnerId(), "Item ownerId should match");
        assertEquals(item.getAvailable(), itemFullDto.getAvailable(), "Item availability should match");
    }

    @Test
    void toItemEntity_ShouldConvertItemDtoToItemEntity() {

        ItemDto itemDto = ItemDto.builder()
                .id(1L)
                .name("Test Item")
                .description("Item Description")
                .ownerId(1L)
                .available(true)
                .build();


        Item item = itemMapper.toItemEntity(itemDto, 1L, itemRequest);

        assertEquals(itemDto.getId(), item.getId(), "ItemDto ID should match Item ID");
        assertEquals(itemDto.getName(), item.getName(), "ItemDto name should match Item name");
        assertEquals(itemDto.getDescription(), item.getDescription(), "ItemDto description should match Item description");
        assertEquals(itemDto.getAvailable(), item.getAvailable(), "ItemDto availability should match Item availability");
        assertEquals(itemDto.getOwnerId(), item.getOwnerId(), "ItemDto ownerId should match Item ownerId");
        assertEquals(itemRequest, item.getRequest(), "Item request should match the provided request");
    }
}