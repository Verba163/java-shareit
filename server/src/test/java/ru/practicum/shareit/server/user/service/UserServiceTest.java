package ru.practicum.shareit.server.user.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import ru.practicum.shareit.server.exception.NotFoundException;
import ru.practicum.shareit.server.user.dto.UserDto;
import ru.practicum.shareit.server.user.mapper.UserMapper;
import ru.practicum.shareit.server.user.model.User;
import ru.practicum.shareit.server.user.storage.UserRepository;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    private User user;
    private UserDto userDto;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User(1L, "John Doe", "john@example.com");
        userDto = new UserDto(1L, "John Doe", "john@example.com");

    }

    @Test
    public void testAddUser() {
        when(userRepository.save(ArgumentMatchers.any(User.class))).thenReturn(user);
        when(userMapper.toUserDto(user)).thenReturn(userDto);

        UserDto result = userService.addUser(user);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(userDto, result);
        verify(userRepository).save(user);
    }

    @Test
    public void testGetAllUsers() {
        when(userRepository.findAll()).thenReturn(Collections.singletonList(user));
        when(userMapper.toUserDto(user)).thenReturn(userDto);

        Collection<UserDto> result = userService.getAllUsers();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
        Assertions.assertTrue(result.contains(userDto));
        verify(userRepository).findAll();
    }

    @Test
    public void testDeleteUser() {
        when(userRepository.existsById(1L)).thenReturn(true);

        userService.deleteUser(1L);

        verify(userRepository).deleteById(1L);
    }

    @Test
    public void deleteUser_ShouldThrowNotFoundException_WhenUserDoesNotExist() {

        long userId = 1L;
        when(userRepository.existsById(userId)).thenReturn(false);

        assertThatThrownBy(() -> {
            userService.deleteUser(userId);
        }).isInstanceOf(NotFoundException.class)
                .hasMessage(String.format("User with id %d not found", userId));

        verify(userRepository, never()).deleteById(userId);
    }

    @Test
    public void testUpdateUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toUserDto(user)).thenReturn(userDto);

        UserDto result = userService.updateUser(1L, userDto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(userDto, result);
        verify(userRepository).save(user);
    }

    @Test
    public void testGetUserById() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toUserDto(user)).thenReturn(userDto);

        UserDto result = userService.getUserById(1L);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(userDto, result);
        verify(userRepository).findById(1L);
    }

    @Test
    public void testGetUserById_ShouldThrowNotFoundException() {

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserById(1L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("User with id 1 not found");

        verify(userRepository).findById(1L);
    }

    @Test
    public void testUserExists() {
        when(userRepository.existsById(1L)).thenReturn(true);

        boolean exists = userService.userExists(1L);

        Assertions.assertTrue(exists);
        verify(userRepository).existsById(1L);
    }
}
