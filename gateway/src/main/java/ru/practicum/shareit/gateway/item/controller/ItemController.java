package ru.practicum.shareit.gateway.item.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.gateway.item.client.ItemClient;
import ru.practicum.shareit.gateway.item.dto.CommentsDto;
import ru.practicum.shareit.gateway.item.dto.ItemDto;

/**
 * TODO Sprint add-controllers.
 */

@RestController
@RequestMapping("/items")
@Slf4j
@AllArgsConstructor
public final class ItemController {

    public static final String ITEM_ID = "/{id}";
    public static final String X_USER_ID_HEADER = "X-Sharer-User-Id";
    private final ItemClient itemClient;

    @GetMapping
    public ResponseEntity<Object> getAllItemsByOwner(@RequestHeader(X_USER_ID_HEADER) Long userId) {
        log.debug("Received GET request for all items");
        return itemClient.getAllItemsByOwner(userId);
    }

    @PostMapping
    public ResponseEntity<Object> addItem(@Valid @RequestHeader(X_USER_ID_HEADER) Long userId,
                                          @RequestBody final ItemDto itemDto) {
        log.debug("Received POST request to add a item: {}", itemDto.getName());
        return itemClient.addItem(userId, itemDto);
    }

    @PatchMapping(ITEM_ID)
    public ResponseEntity<Object> updateItem(@RequestBody ItemDto itemDto, @PathVariable("id") final long id,
                                             @RequestHeader(X_USER_ID_HEADER) Long userId) {

        log.debug("Received request PATCH for item update with id: {}", id);

        return itemClient.updateItem(userId, itemDto);
    }

    @GetMapping(ITEM_ID)
    public ResponseEntity<Object> getItemById(@RequestHeader(X_USER_ID_HEADER) Long userId,
                                              @PathVariable final long id) {
        log.debug("Received GET request for items with id: {}", id);
        return itemClient.getItemById(userId, id);
    }

    @DeleteMapping(ITEM_ID)
    public void deleteItem(@PathVariable("id") final long id) {
        log.debug("Received DELETE request to remove item with id {}", id);
        itemClient.deleteItem(id);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItems(
            @RequestParam("text") String query,
            @RequestHeader("X-Sharer-User -Id") Long userId) {
        log.debug("Received GET request for items search with query: {}", query);
        return itemClient.searchItemsByQuery(query.trim(), userId);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@Valid @RequestHeader(X_USER_ID_HEADER) Long userId,
                                                @PathVariable Long itemId,
                                                @RequestBody CommentsDto commentDto) {
        log.debug("Received POST request for creating comments for item with ID: {}", itemId);
        return itemClient.createComment(itemId, userId, commentDto);
    }
}

