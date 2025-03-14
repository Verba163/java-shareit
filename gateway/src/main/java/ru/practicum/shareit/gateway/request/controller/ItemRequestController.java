package ru.practicum.shareit.gateway.request.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.gateway.request.client.ItemRequestClient;
import ru.practicum.shareit.gateway.request.dto.ItemRequestDto;

/**
 * TODO Sprint add-item-requests.
 */

@RestController
@Slf4j
@RequestMapping(path = "/requests")
@AllArgsConstructor
public class ItemRequestController {
    ItemRequestClient itemRequestClient;
    public static final String X_USER_ID_HEADER = "X-Sharer-User-Id";
    public static final String ITEM_REQUEST_ID_PATH = "/{request-Id}";
    public static final String ITEM_REQUEST_ID = "request-Id";

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> addItemRequest(@Valid @RequestBody final ItemRequestDto itemRequestDto,
                                                 @RequestHeader(X_USER_ID_HEADER) Long userId) {
        log.debug("Received POST request to add a item request: {}", itemRequestDto.getId());

        return itemRequestClient.createItemRequest(userId, itemRequestDto);
    }

    @GetMapping
    public ResponseEntity<Object> getAllItemRequestsByUser(@RequestHeader(X_USER_ID_HEADER) Long userId) {

        log.debug("Received GET item request for user with id: {}", userId);
        return itemRequestClient.getAllItemRequestsByUser(userId);
    }

    @GetMapping(ITEM_REQUEST_ID_PATH)
    public ResponseEntity<Object> getItemRequestById(@RequestHeader(X_USER_ID_HEADER) Long userId,
                                                     @PathVariable(ITEM_REQUEST_ID) Long requestId) {

        log.debug("Received GET request for item request with id: {}", requestId);
        return itemRequestClient.getItemRequestById(userId, requestId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllItemRequests() {
        log.debug("Received GET request for all item requests");
        return itemRequestClient.getAllItemRequests();
    }

}