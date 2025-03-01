package ru.practicum.shareit.user.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserDto addUser(User user) {
        User addedUser = userRepository.save(user);
        return userMapper.toUserDto(addedUser);
    }

    @Transactional
    public Collection<UserDto> getAllUsers() {
        log.info("Getting all Users");
        return userRepository.findAll().stream()
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteUser(long id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException(String.format("User with id %d not found", id));
        }
        userRepository.deleteById(id);
    }

    @Transactional
    public UserDto updateUser(Long id, UserDto userDto) {
        log.info("Updating user with id: {}", id);

        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User  not found with id: " + id));

        if (userDto.getName() != null) {
            existingUser.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            existingUser.setEmail(userDto.getEmail());
        }

        User updatedUser = userRepository.save(existingUser);
        log.info("User  updated successfully: {}", updatedUser);

        return userMapper.toUserDto(updatedUser);
    }

    @Transactional
    public UserDto getUserById(long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %d not found", id)));
        return userMapper.toUserDto(user);
    }

    @Transactional
    public boolean userExists(long id) {
        return userRepository.existsById(id);
    }

}

