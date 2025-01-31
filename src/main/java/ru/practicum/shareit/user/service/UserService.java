package ru.practicum.shareit.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.InMemoryUserStorage;

import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {
    private final InMemoryUserStorage storage;
    private final UserMapper mapper;

    @Autowired
    public UserService(InMemoryUserStorage storage, UserMapper mapper) {
        this.storage = storage;
        this.mapper = mapper;
    }

    public UserDto addUser(User user) {
        User addedUser = storage.addUser(user);
        return mapper.toDto(addedUser);
    }


    public Collection<UserDto> getAllUsers() {
        return storage.getAllUsers().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }


    public void deleteUser(long id) {
        storage.deleteUser(id);
    }

    public UserDto updateUser(User user) {
        User updatedUser = storage.updateUser(user);
        return mapper.toDto(updatedUser);
    }

    public UserDto getUserById(long id) {
        User user = storage.getUserById(id);
        return mapper.toDto(user);
    }

    public boolean userExists(long id) {
        return storage.userExists(id);
    }
}

