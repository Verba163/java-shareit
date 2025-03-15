package ru.practicum.shareit.server.item.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.server.item.dto.ItemDto;
import ru.practicum.shareit.server.item.dto.ItemFullDto;
import ru.practicum.shareit.server.item.model.Item;
import ru.practicum.shareit.server.request.model.ItemRequest;

@Component
public class ItemMapper {

    public ItemDto toItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .ownerId(item.getOwnerId())
                .available(item.getAvailable())
                .requestId(item.getRequest() != null ? item.getRequest().getId() : null)
                .build();
    }

    public ItemFullDto toItemFullDto(Item item) {
        return ItemFullDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .ownerId(item.getOwnerId())
                .available(item.getAvailable())
                .build();

    }

    public Item toItemEntity(ItemDto itemDto, Long ownerId, ItemRequest request) {
        return Item.builder()
                .id(itemDto.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .ownerId(itemDto.getOwnerId())
                .request(request)
                .build();
    }
}


