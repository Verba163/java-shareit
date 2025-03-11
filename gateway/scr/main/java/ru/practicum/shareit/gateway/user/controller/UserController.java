package ru.practicum.shareit.gateway.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.gateway.user.client.UserClient;
import ru.practicum.shareit.gateway.user.dto.UserDto;


@RestController
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
public final class UserController {

    public static final String USER_ID = "/{id}";
    private final UserClient userClient;

    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        log.debug("Received GET request for all users");
        return userClient.getAllUsers();
    }

    @GetMapping(USER_ID)
    public ResponseEntity<Object> getUserById(@PathVariable("id") final long id) {
        log.debug("Received GET request for user with id {}", id);
        return userClient.getUserById(id);
    }

    @PostMapping
    public ResponseEntity<Object> addUser(@Valid @RequestBody UserDto userDto) {
        log.debug("Received POST request to add a user: {}", userDto.getName());
        return userClient.addUser(userDto);
    }

    @PatchMapping(USER_ID)
    public ResponseEntity<Object> updateUser(@RequestBody UserDto userDto,
                                             @PathVariable("id") Long id) {
        log.debug("Received PATCH request to update a user with id: {}", id);
        return userClient.updateUser(userDto, id);
    }

    @DeleteMapping(USER_ID)
    public void deleteUser(@PathVariable("id") final long id) {
        log.debug("Received DELETE request to remove user with id {}", id);
        userClient.deleteUser(id);
    }

}