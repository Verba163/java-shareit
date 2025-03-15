package ru.practicum.shareit.gateway.request.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.gateway.client.BaseClient;
import ru.practicum.shareit.gateway.request.dto.ItemRequestDto;


@Service
public class ItemRequestClient extends BaseClient {
    private static final String API_PREFIX = "/requests";

    @Autowired
    public ItemRequestClient(@Value(BaseClient.SHAREIT_SERVER_URL) String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }


    public ResponseEntity<Object> createItemRequest(Long userId, ItemRequestDto itemRequestDto) {
        return post("", userId, itemRequestDto);
    }

    public ResponseEntity<Object> getItemRequestById(Long userId, Long requestId) {
        return get("/" + requestId, userId);
    }

    public ResponseEntity<Object> getAllItemRequestsByUser(Long userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> getAllItemRequests() {
        return get("/all");
    }
}