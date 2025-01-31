package ru.practicum.shareit.item.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.InMemoryItemStorage;

import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ItemService {
    private final InMemoryItemStorage storage;
    private final ItemMapper mapper;

    public ItemService(InMemoryItemStorage storage, ItemMapper mapper) {
        this.storage = storage;
        this.mapper = mapper;
    }

    public ItemDto addItem(Item item) {
        Item addedItem = storage.addItem(item);
        return mapper.toDto(addedItem);
    }

    public Collection<ItemDto> getAllItems() {
        return storage.getAllItems().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public ItemDto updateItem(Item item) {
        Item updatedItem = storage.updateItem(item);
        return mapper.toDto(updatedItem);
    }

    public void deleteItem(long id) {
        storage.deleteItem(id);
    }

    public ItemDto getItemById(long id) {
        Item item = storage.getItemById(id);
        return mapper.toDto(item);
    }

    public Collection<ItemDto> getAllItemsByOwner(Long userId) {
        return storage.getAllItems().stream()
                .filter(item -> item.getOwnerId() != null && item.getOwnerId().equals(userId))
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public Collection<ItemDto> searchItemsByQuery(String query) {
        return storage.searchItemsByQuery(query).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }
}
