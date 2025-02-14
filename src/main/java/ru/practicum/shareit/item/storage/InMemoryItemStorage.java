package ru.practicum.shareit.item.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.exception.NotFoundException;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class InMemoryItemStorage implements ItemStorage {
    long currentId = 1;
    Map<Long, Item> items = new HashMap<>();

    @Override
    public Item addItem(Item item) {
        item.setId(currentId++);
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Collection<Item> getAllItems() {
        log.debug("Got all Items. Total: {}", items.size());
        return items.values();
    }

    @Override
    public Item updateItem(Item item) {
        if (!items.containsKey(item.getId())) {
            throw new NotFoundException(String.format("Item with ID %d not found. Cannot update.", item.getId()));
        }
        items.put(item.getId(), item);
        log.debug("Item with Id {} updated", item.getId());
        return item;
    }

    @Override
    public void deleteItem(long id) {
        Item item = items.get(id);
        if (item == null) {
            throw new NotFoundException(String.format("Item with id %d not found", id));
        }
        log.debug("Item with Id {} deleted", id);
        items.remove(id);
    }

    @Override
    public Item getItemById(long id) {
        Item item = items.get(id);
        if (item == null) {
            throw new NotFoundException(String.format("Item with id %d not found", id));
        }
        log.debug("Received Item with Id {}", id);
        return item;
    }

    @Override
    public Collection<Item> searchItemsByQuery(String query) {
        if (query == null || query.isEmpty()) {
            log.debug("Request field is empty");
            return Collections.emptyList();
        }

        return getAllItems().stream()
                .filter(item -> item.getAvailable() != null && item.getAvailable())
                .filter(item -> item.getName() != null &&
                        item.getName().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
    }
}