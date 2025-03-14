package ru.practicum.shareit.server.user.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.server.user.dto.UserDto;
import ru.practicum.shareit.server.user.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserMapperTest {

    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        userMapper = new UserMapper();
    }

    @Test
    void toUserDtoShouldConvertUserToUserDto() {

        User user = User.builder()
                .id(1L)
                .name("Alice")
                .email("alice@example.com")
                .build();

        UserDto userDto = userMapper.toUserDto(user);

        assertEquals(user.getId(), userDto.getId(), "User  ID should match");
        assertEquals(user.getName(), userDto.getName(), "User  name should match");
        assertEquals(user.getEmail(), userDto.getEmail(), "User  email should match");
    }

    @Test
    void toUserEntityShouldConvertUserDtoToUser() {

        UserDto userDto = UserDto.builder()
                .id(1L)
                .name("Bob")
                .email("bob@example.com")
                .build();

        User user = userMapper.toUserEntity(userDto);

        assertEquals(userDto.getId(), user.getId(), "User Dto ID should match User ID");
        assertEquals(userDto.getName(), user.getName(), "User Dto name should match User name");
        assertEquals(userDto.getEmail(), user.getEmail(), "User Dto email should match User email");
    }
}