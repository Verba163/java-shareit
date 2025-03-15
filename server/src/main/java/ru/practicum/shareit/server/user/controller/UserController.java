package ru.practicum.shareit.server.user.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.server.user.dto.UserDto;
import ru.practicum.shareit.server.user.model.User;
import ru.practicum.shareit.server.user.service.UserService;

import java.util.Collection;

@RestController
@RequestMapping("/users")
@Slf4j
@AllArgsConstructor
public final class UserController {

    public static final String USER_ID_PATH = "/{user-id}";
    public static final String USER_ID = "user-id";
    private final UserService service;

    @GetMapping
    public Collection<UserDto> getAllUsers() {
        log.debug("Received GET request for all users");
        return service.getAllUsers();
    }

    @GetMapping(USER_ID_PATH)
    public UserDto getUserById(@PathVariable(USER_ID) final long id) {
        log.debug("Received GET request for user with id {}", id);
        return service.getUserById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto addUser(@RequestBody final User user) {
        log.debug("Received POST request to add a user: {}", user.getName());
        return service.addUser(user);
    }

    @PatchMapping(USER_ID_PATH)
    public UserDto updateUser(@RequestBody final UserDto userDto,
                              @PathVariable(USER_ID) final long id) {
        log.debug("Received PATCH request to update a user with id: {}", id);
        return service.updateUser(id, userDto);
    }

    @DeleteMapping(USER_ID_PATH)
    public void deleteUser(@PathVariable(USER_ID) final long id) {
        log.debug("Received DELETE request to remove user with id {}", id);
        service.deleteUser(id);
    }

}