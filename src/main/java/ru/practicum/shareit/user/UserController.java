package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collection;

@RestController
@RequestMapping("/users")
@Slf4j
public final class UserController {

    public static final String USER_ID = "/{id}";
    private final UserService service;

    @Autowired
    public UserController(final UserService userService) {
        this.service = userService;
    }

    @GetMapping
    public Collection<UserDto> getAllUsers() {
        log.debug("Received GET request for all users");
        return service.getAllUsers();
    }

    @GetMapping(USER_ID)
    public UserDto getUserById(@PathVariable("id") final long id) {
        log.debug("Received GET request for user with id {}", id);
        return service.getUserById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto addUser(@Valid @RequestBody final User user) {
        log.debug("Received POST request to add a user: {}", user.getName());
        return service.addUser(user);
    }

    @PatchMapping(USER_ID)
    public UserDto updateUser(@RequestBody final User user,
                              @PathVariable("id") final long id) {
        log.debug("Received PATCH request to update a user with id: {}", id);
        user.setId(id);
        return service.updateUser(user);
    }

    @DeleteMapping(USER_ID)
    public void deleteUser(@PathVariable("id") final long id) {
        log.debug("Received DELETE request to remove user with id {}", id);
        service.deleteUser(id);
    }

}