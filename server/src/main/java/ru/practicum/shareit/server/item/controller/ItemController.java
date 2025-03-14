package ru.practicum.shareit.server.item.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.server.item.dto.CommentsDto;
import ru.practicum.shareit.server.item.dto.ItemDto;
import ru.practicum.shareit.server.item.dto.ItemFullDto;
import ru.practicum.shareit.server.item.model.Item;
import ru.practicum.shareit.server.item.service.ItemService;
import ru.practicum.shareit.server.user.service.UserService;

import java.util.Collection;

/**
 * TODO Sprint add-controllers.
 */

@RestController
@RequestMapping("/items")
@Slf4j
@AllArgsConstructor
public final class ItemController {

    public static final String ITEM_ID_PATH = "/{item-Id}";
    public static final String X_USER_ID_HEADER = "X-Sharer-User-Id";
    public static final String ITEM_ID = "item-Id";
    private final ItemService service;
    private final UserService userService;

    @GetMapping
    public Collection<ItemDto> getAllItemsByOwner(@RequestHeader(X_USER_ID_HEADER) Long userId) {
        log.debug("Received GET request for all items");
        return service.getAllItemsByOwner(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto addItem(@RequestHeader(X_USER_ID_HEADER) Long userId,
                           @RequestBody final ItemDto itemDto) {
        log.debug("Received POST request to add a item: {}", itemDto.getName());

        if (!userService.userExists(userId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("User not found with ID: %d", userId));
        }

        itemDto.setOwnerId(userId);
        return service.addItem(userId, itemDto);
    }

    @PatchMapping(ITEM_ID_PATH)
    public ItemDto updateItem(@PathVariable(ITEM_ID) Long itemId,
                              @RequestBody final Item item,
                              @RequestHeader(X_USER_ID_HEADER) Long userId) {

        log.debug("Received request PATCH for item update with id: {}", itemId);

        ItemDto existingItem = service.findItemById(itemId);
        if (existingItem.getOwnerId() == null || !existingItem.getOwnerId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to update this item");
        }

        item.setId(itemId);
        item.setOwnerId(existingItem.getOwnerId());
        return service.updateItem(item);
    }

    @DeleteMapping(ITEM_ID_PATH)
    public void deleteItem(@PathVariable(ITEM_ID) Long itemId) {
        log.debug("Received DELETE request to remove item with id {}", itemId);
        service.deleteItem(itemId);
    }

    @GetMapping("/search")
    public Collection<ItemDto> searchItems(
            @RequestParam("text") String query) {
        log.debug("Received GET request for items search with query: {}", query);
        return service.searchItemsByQuery(query.trim());
    }

    @GetMapping(ITEM_ID_PATH)
    public ItemFullDto getItemById(@RequestHeader(X_USER_ID_HEADER) Long userId,
                                   @PathVariable(ITEM_ID) final long id) {
        log.debug("Received GET request for items with id: {}", id);

        return service.getItemById(userId, id);
    }

    @PostMapping("/{item-Id}/comment")
    public CommentsDto createComment(@RequestHeader(X_USER_ID_HEADER) Long userId,
                                     @PathVariable(ITEM_ID) Long itemId,
                                     @RequestBody CommentsDto commentDto) {
        log.debug("Received POST request for creating comments for item with ID: {}", itemId);
        return service.createComments(userId, itemId, commentDto);
    }
}

