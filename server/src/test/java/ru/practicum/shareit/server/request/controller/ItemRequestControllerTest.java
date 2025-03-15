package ru.practicum.shareit.server.request.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.server.request.dto.ItemRequestDto;
import ru.practicum.shareit.server.request.service.ItemRequestService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemRequestController.class)
public class ItemRequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemRequestService itemRequestService;

    @InjectMocks
    private ItemRequestController itemRequestController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    public void testAddItemRequest() throws Exception {
        ItemRequestDto itemRequestDto = new ItemRequestDto(1L, "Test Description", 1L, Collections.emptyList(), LocalDateTime.now());

        when(itemRequestService.createItemRequest(any(Long.class), any(ItemRequestDto.class)))
                .thenReturn(itemRequestDto);

        mockMvc.perform(post("/requests")
                        .header(ItemRequestController.X_USER_ID_HEADER, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.description").value("Test Description"));

        verify(itemRequestService, times(1)).createItemRequest(any(Long.class), any(ItemRequestDto.class));
    }

    @Test
    public void testGetAllItemRequestsByUser() throws Exception {
        List<ItemRequestDto> itemRequestDtos = Collections.singletonList(new ItemRequestDto(1L, "Test Description", 1L, Collections.emptyList(), LocalDateTime.now()));

        when(itemRequestService.getAllItemRequestsByUser(any(Long.class))).thenReturn(itemRequestDtos);

        mockMvc.perform(get("/requests")
                        .header(ItemRequestController.X_USER_ID_HEADER, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].description").value("Test Description"));

        verify(itemRequestService, times(1)).getAllItemRequestsByUser(any(Long.class));
    }

    @Test
    public void testGetItemRequestById() throws Exception {
        ItemRequestDto itemRequestDto = new ItemRequestDto(1L, "Test Description", 1L, Collections.emptyList(), LocalDateTime.now());

        when(itemRequestService.getItemRequestById(any(Long.class), any(Long.class)))
                .thenReturn(itemRequestDto);

        mockMvc.perform(get("/requests/1")
                        .header(ItemRequestController.X_USER_ID_HEADER, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.description").value("Test Description"));

        verify(itemRequestService, times(1)).getItemRequestById(any(Long.class), any(Long.class));
    }

    @Test
    public void testGetAllItemRequests() throws Exception {
        List<ItemRequestDto> itemRequestDto = Collections.singletonList(new ItemRequestDto(1L, "Test Description", 1L, Collections.emptyList(), LocalDateTime.now()));

        when(itemRequestService.getAllItemRequests()).thenReturn(itemRequestDto);

        mockMvc.perform(get("/requests/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].description").value("Test Description"));

        verify(itemRequestService, times(1)).getAllItemRequests();
    }
}
