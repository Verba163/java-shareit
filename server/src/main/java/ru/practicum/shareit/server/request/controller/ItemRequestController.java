package ru.practicum.shareit.server.request.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.server.request.dto.ItemRequestDto;
import ru.practicum.shareit.server.request.service.ItemRequestService;

import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */

@RestController
@Slf4j
@RequestMapping(path = "/requests")
@AllArgsConstructor
public class ItemRequestController {
    ItemRequestService itemRequestService;
    public static final String X_USER_ID_HEADER = "X-Sharer-User-Id";

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemRequestDto addItemRequest(@RequestBody final ItemRequestDto itemRequestDto,
                                         @RequestHeader(X_USER_ID_HEADER) Long userId) {
        log.debug("Received POST request to add a item request: {}", itemRequestDto.getId());

        return itemRequestService.createItemRequest(userId, itemRequestDto);
    }

    @GetMapping
    public List<ItemRequestDto> getAllItemRequestsByUser(@RequestHeader(X_USER_ID_HEADER) Long userId) {

        log.debug("Received GET item request for user with id: {}", userId);
        return itemRequestService.getAllItemRequestsByUser(userId);
    }

    @GetMapping("/{request-Id}")
    public ItemRequestDto getItemRequestById(@RequestHeader(X_USER_ID_HEADER) Long userId,
                                             @PathVariable("request-Id") Long requestId) {

        log.debug("Received GET request for item request with id: {}", requestId);
        return itemRequestService.getItemRequestById(userId, requestId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAllItemRequests() {
        log.debug("Received GET request for all item requests");
        return itemRequestService.getAllItemRequests();
    }

}
