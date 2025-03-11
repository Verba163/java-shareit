package ru.practicum.shareit.server.request.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.server.exception.NotFoundException;
import ru.practicum.shareit.server.item.dto.ItemDto;
import ru.practicum.shareit.server.item.service.ItemService;
import ru.practicum.shareit.server.request.dto.ItemRequestDto;
import ru.practicum.shareit.server.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.server.request.model.ItemRequest;
import ru.practicum.shareit.server.request.storage.ItemRequestRepository;
import ru.practicum.shareit.server.user.model.User;
import ru.practicum.shareit.server.user.storage.UserRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRequestMapper itemRequestMapper;
    private final ItemService itemService;

    public ItemRequestDto createItemRequest(Long userId, ItemRequestDto itemRequestDto) {

        User requester = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %d not found", userId)));

        ItemRequest itemRequest = itemRequestMapper.toItemRequestEntity(itemRequestDto, requester);

        itemRequestRepository.save(itemRequest);

        return itemRequestMapper.itemRequestToDtoWithItems(itemRequest, List.of());
    }

    public ItemRequestDto getItemRequestById(Long userId, Long requestId) {

        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %d not found", userId)));

        ItemRequest itemRequest = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException(String.format("Item request with id %d not found", requestId)));

        List<ItemDto> items = itemService.getItemsByRequestId(requestId);

        return itemRequestMapper.itemRequestToDtoWithItems(itemRequest, items);
    }

    @Transactional
    public List<ItemRequestDto> getAllItemRequestsByUser(Long userId) {

        User requester = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %d not found", userId)));

        List<ItemRequest> allRequestsByUser = itemRequestRepository.findByRequester(requester);

        return allRequestsByUser.stream()
                .map(itemRequestMapper::itemRequestToDto)
                .collect(Collectors.toList());

    }

    @Transactional
    public List<ItemRequestDto> getAllItemRequests() {

        return itemRequestRepository.findAll().stream()
                .sorted(Comparator.comparing(ItemRequest::getCreated).reversed())
                .map(itemRequestMapper::itemRequestToDto)
                .collect(Collectors.toList());

    }
}
