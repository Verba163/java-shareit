package ru.practicum.shareit.server.item.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.server.item.dto.ItemDto;
import ru.practicum.shareit.server.item.model.Item;
import ru.practicum.shareit.server.item.service.ItemService;
import ru.practicum.shareit.server.user.service.UserService;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private ItemService itemService;

    @Mock
    private UserService userService;

    @InjectMocks
    private ItemController itemController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(itemController).build();
    }

    @Test
    public void testGetAllItemsByOwner() throws Exception {
        when(itemService.getAllItemsByOwner(1L)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/items")
                        .header(ItemController.X_USER_ID_HEADER, 1L))
                .andExpect(status().isOk());

        verify(itemService, times(1)).getAllItemsByOwner(1L);
    }

    @Test
    public void testAddItem() throws Exception {
        ItemDto itemDto = new ItemDto();
        itemDto.setName("New Item");
        itemDto.setDescription("Item Description");
        itemDto.setOwnerId(1L);

        when(userService.userExists(1L)).thenReturn(true);
        when(itemService.addItem(any(Long.class), any(ItemDto.class))).thenReturn(itemDto);

        mockMvc.perform(post("/items")
                        .header(ItemController.X_USER_ID_HEADER, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(itemDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("New Item"));

        verify(userService, times(1)).userExists(1L);
        verify(itemService, times(1)).addItem(1L, itemDto);
    }

    @Test
    public void testUpdateItem() throws Exception {
        Item item = new Item();
        item.setId(1L);
        item.setName("Updated Item");

        when(itemService.findItemById(1L)).thenReturn(new ItemDto(1L, "Item", "Description", 1L, true, 1L));
        when(itemService.updateItem(any(Item.class))).thenReturn(new ItemDto(1L, "Updated Item", "Description", 1L, true, 1L));

        mockMvc.perform(patch("/items/1")
                        .header(ItemController.X_USER_ID_HEADER, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(item)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Item"));

        verify(itemService, times(1)).findItemById(1L);
        verify(itemService, times(1)).updateItem(any(Item.class));
    }

    @Test
    public void testDeleteItem() throws Exception {
        mockMvc.perform(delete("/items/1"))
                .andExpect(status().isOk());

        verify(itemService, times(1)).deleteItem(1L);
    }

    @Test
    public void testSearchItems() throws Exception {
        when(itemService.searchItemsByQuery("query")).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/items/search")
                        .param("text", "query"))
                .andExpect(status().isOk());

        verify(itemService, times(1)).searchItemsByQuery("query");
    }

}