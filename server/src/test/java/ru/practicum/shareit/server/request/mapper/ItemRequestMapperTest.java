package ru.practicum.shareit.server.request.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.server.item.dto.ItemDto;
import ru.practicum.shareit.server.request.dto.ItemRequestDto;
import ru.practicum.shareit.server.request.model.ItemRequest;
import ru.practicum.shareit.server.user.model.User;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ItemRequestMapperTest {

    private ItemRequestMapper itemRequestMapper;

    @BeforeEach
    void setUp() {
        itemRequestMapper = new ItemRequestMapper();
    }

    @Test
    void itemRequestToDtoWithItems_ShouldConvertItemRequestToDtoWithItems() {

        User requester = User.builder().id(1L).build();
        ItemRequest itemRequest = ItemRequest.builder()
                .id(1L)
                .description("Need a new item")
                .created(LocalDateTime.now())
                .requester(requester)
                .build();

        ItemDto itemDto = ItemDto.builder().id(1L).name("Item 1").build();
        List<ItemDto> items = Collections.singletonList(itemDto);

        ItemRequestDto itemRequestDto = itemRequestMapper.itemRequestToDtoWithItems(itemRequest, items);

        assertEquals(itemRequest.getId(), itemRequestDto.getId(), "ItemRequest ID should match");
        assertEquals(itemRequest.getDescription(), itemRequestDto.getDescription(), "ItemRequest description should match");
        assertEquals(itemRequest.getRequester().getId(), itemRequestDto.getRequester(), "Requester ID should match");
        assertEquals(items, itemRequestDto.getItems(), "Items list should match");
    }

    @Test
    void toItemRequestEntity_ShouldConvertItemRequestDtoToItemRequestEntity() {

        ItemRequestDto itemRequestDto = ItemRequestDto.builder()
                .id(1L)
                .description("Need a new item")
                .created(LocalDateTime.now())
                .requester(1L)
                .build();

        User requester = User.builder().id(1L).build();

        ItemRequest itemRequest = itemRequestMapper.toItemRequestEntity(itemRequestDto, requester);

        assertEquals(itemRequestDto.getId(), itemRequest.getId(), "ItemRequestDto ID should match ItemRequest ID");
        assertEquals(itemRequestDto.getDescription(), itemRequest.getDescription(), "ItemRequestDto description should match ItemRequest description");
        assertEquals(requester, itemRequest.getRequester(), "Requester should match the provided requester");
        assertEquals(LocalDateTime.now().getMinute(), itemRequest.getCreated().getMinute(), "Created time should be set to now");
    }

    @Test
    void itemRequestToDto_ShouldConvertItemRequestToDto() {

        User requester = User.builder().id(1L).build();
        ItemRequest itemRequest = ItemRequest.builder()
                .id(1L)
                .description("Need a new item")
                .created(LocalDateTime.now())
                .requester(requester)
                .build();


        ItemRequestDto itemRequestDto = itemRequestMapper.itemRequestToDto(itemRequest);

        assertEquals(itemRequest.getId(), itemRequestDto.getId(), "ItemRequest ID should match");
        assertEquals(itemRequest.getDescription(), itemRequestDto.getDescription(), "ItemRequest description should match");
        assertEquals(itemRequest.getRequester().getId(), itemRequestDto.getRequester(), "Requester ID should match");
        assertTrue(Duration.between(LocalDateTime.now(), itemRequest.getCreated()).toMinutes() < 1, "Created time should be set to now");
    }
}
