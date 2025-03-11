package ru.practicum.shareit.server.request.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.server.item.dto.ItemDto;
import ru.practicum.shareit.server.request.dto.ItemRequestDto;
import ru.practicum.shareit.server.request.model.ItemRequest;
import ru.practicum.shareit.server.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Component
@AllArgsConstructor
public class ItemRequestMapper {

    public ItemRequestDto itemRequestToDtoWithItems(ItemRequest itemRequest, List<ItemDto> items) {
        return ItemRequestDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated())
                .requester(itemRequest.getRequester().getId())
                .items(items)
                .build();
    }

    public ItemRequest toItemRequestEntity(ItemRequestDto itemRequestDto, User requester) {
        return ItemRequest.builder()
                .id(itemRequestDto.getId())
                .description(itemRequestDto.getDescription())
                .requester(requester)
                .created(LocalDateTime.now())
                .build();
    }

    public ItemRequestDto itemRequestToDto(ItemRequest itemRequest) {
        return ItemRequestDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated())
                .requester(itemRequest.getRequester().getId())
                .build();
    }
}
