package ru.practicum.shareit.server.user.controller;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.server.user.dto.UserDto;
import ru.practicum.shareit.server.user.model.User;
import ru.practicum.shareit.server.user.service.UserService;

import java.util.Arrays;
import java.util.Collection;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService service;

    @InjectMocks
    private UserController userController;

    @Test
    public void getAllUsers_ShouldReturnUsersList() throws Exception {
        UserDto user1 = new UserDto(1L, "User1", "Email1@test.ru");
        UserDto user2 = new UserDto(2L, "User2", "Email2@test.ru");
        Collection<UserDto> users = Arrays.asList(user1, user2);

        when(service.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("User1"))
                .andExpect(jsonPath("$[1].name").value("User2"));

        verify(service, times(1)).getAllUsers();
    }

    @Test
    public void getUserById_ShouldReturnUserById() throws Exception {
        UserDto user = new UserDto(1L, "User1", "Email1@test.ru");

        when(service.getUserById(1L)).thenReturn(user);

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("User1"));

        verify(service, times(1)).getUserById(1L);
    }

    @Test
    public void addUser_ShouldReturnAddedUser() throws Exception {
        User user = new User(1L, "User1", "Email1@test.ru");
        UserDto userDto = new UserDto(1L, "User1", "Email1@test.ru");

        when(service.addUser(any(User.class))).thenReturn(userDto);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"User1\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("User1"));

        verify(service, times(1)).addUser(any(User.class));
    }

    @Test
    public void updateUser_ShouldUpdateUserFields() throws Exception {
        UserDto updatedUserDto = new UserDto(1L, "Updated User", "Email1@test.ru");

        when(service.updateUser(anyLong(), any(UserDto.class))).thenReturn(updatedUserDto);

        mockMvc.perform(patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Updated User\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Updated User"));

        verify(service, times(1)).updateUser(eq(1L), any(UserDto.class));
    }

    @Test
    public void deleteUser_ShouldDeleteDeletingUser() throws Exception {
        doNothing().when(service).deleteUser(1L);

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isOk());

        verify(service, times(1)).deleteUser(1L);
    }
}
