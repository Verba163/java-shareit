package ru.practicum.shareit.gateway.item.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.gateway.client.BaseClient;
import ru.practicum.shareit.gateway.item.dto.*;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> addItem(Long ownerId, ItemDto itemDto) {
        return post("", ownerId, itemDto);
    }

    public ResponseEntity<Object> getItemById(Long userId, Long itemId) {
        return get("/" + itemId, userId);
    }

    public ResponseEntity<Object> getAllItemsByOwner(Long userId) {
        return get("/owner", userId);
    }

    public ResponseEntity<Object> updateItem(Long ownerId, ItemDto itemDto) {
        return patch("/" + ownerId, itemDto);
    }

    public void deleteItem(Long itemId) {
        delete("", itemId);
    }

    public ResponseEntity<Object> searchItemsByQuery(String query, Long userId) {
        return get("search?text=" + query, userId);
    }

    public ResponseEntity<Object> createComment(Long itemId, Long userId, CommentsDto commentDto) {
        return post("/" + itemId + "/comment", userId, commentDto);
    }

    public ResponseEntity<Object> getItemsByRequestId(Long requestId) {
        return get("/requests/" + requestId);
    }
}

