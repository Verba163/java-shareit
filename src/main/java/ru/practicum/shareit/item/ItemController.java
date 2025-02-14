package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collection;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@Slf4j
public final class ItemController {

    public static final String ITEM_ID = "/{id}";
    public static final String X_USER_ID_HEADER = "X-Sharer-User-Id";
    private final ItemService service;
    private final UserService userService;

    @Autowired
    public ItemController(ItemService service, UserService userService) {
        this.service = service;
        this.userService = userService;
    }

    @GetMapping
    public Collection<ItemDto> getAllItemsByOwner(@RequestHeader(X_USER_ID_HEADER) Long userId) {
        log.debug("Received GET request for all items");
        return service.getAllItemsByOwner(userId);
    }

    @GetMapping(ITEM_ID)
    public ItemDto getItemById(@PathVariable("id") final long id) {
        log.debug("Received GET request for item with id {}", id);
        return service.getItemById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto addItem(@Valid @RequestBody final Item item,
                           @RequestHeader(X_USER_ID_HEADER) Long userId) {
        log.debug("Received POST request to add a item: {}", item.getName());

        if (!userService.userExists(userId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        item.setOwnerId(userId);
        return service.addItem(item);
    }

    @PatchMapping(ITEM_ID)
    public ItemDto updateItem(@RequestBody final Item item, @PathVariable("id") final long id,
                              @RequestHeader(X_USER_ID_HEADER) Long userId) {
        log.debug("Received request PATCH for item update with id: {}", id);

        ItemDto existingItem = service.getItemById(id);
        if (existingItem.getOwnerId() == null || !existingItem.getOwnerId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to update this item");
        }
        item.setId(id);
        return service.updateItem(item);
    }

    @DeleteMapping(ITEM_ID)
    public void deleteItem(@PathVariable("id") final long id) {
        log.debug("Received DELETE request to remove item with id {}", id);
        service.deleteItem(id);
    }

    @GetMapping("/search")
    public Collection<ItemDto> searchItems(
            @RequestParam("text") String query) {
        log.debug("Received GET request for items search with query: {}", query);
        return service.searchItemsByQuery(query.trim());
    }
}

