package ru.practicum.shareit.server.request.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.practicum.shareit.server.item.service.ItemService;
import ru.practicum.shareit.server.request.dto.ItemRequestDto;
import ru.practicum.shareit.server.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.server.request.model.ItemRequest;
import ru.practicum.shareit.server.request.storage.ItemRequestRepository;
import ru.practicum.shareit.server.user.model.User;
import ru.practicum.shareit.server.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ItemRequestServiceTest {

    @InjectMocks
    private ItemRequestService itemRequestService;

    @Mock
    private ItemRequestRepository itemRequestRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRequestMapper itemRequestMapper;

    @Mock
    private ItemService itemService;

    private User user;
    private ItemRequest itemRequest;
    private ItemRequestDto itemRequestDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User(1L, "Test User", "EmailT@Test.ru");
        itemRequest = new ItemRequest(1L, "Test Description", user, LocalDateTime.now());
        itemRequestDto = new ItemRequestDto(1L, "Test Description", 1L, Collections.emptyList(), LocalDateTime.now());
    }

    @Test
    public void testCreateItemRequest() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(itemRequestMapper.toItemRequestEntity(any(), any())).thenReturn(itemRequest);
        when(itemRequestMapper.itemRequestToDtoWithItems(any(), any())).thenReturn(itemRequestDto);

        ItemRequestDto result = itemRequestService.createItemRequest(1L, itemRequestDto);

        assertEquals(itemRequestDto, result);
        verify(itemRequestRepository).save(itemRequest);
    }

    @Test
    public void testGetItemRequestById() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(itemRequestRepository.findById(1L)).thenReturn(Optional.of(itemRequest));
        when(itemService.getItemsByRequestId(1L)).thenReturn(Collections.emptyList());
        when(itemRequestMapper.itemRequestToDtoWithItems(any(), any())).thenReturn(itemRequestDto);

        ItemRequestDto result = itemRequestService.getItemRequestById(1L, 1L);

        assertEquals(itemRequestDto, result);
    }

    @Test
    public void testGetAllItemRequestsByUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(itemRequestRepository.findByRequester(user)).thenReturn(Collections.singletonList(itemRequest));
        when(itemRequestMapper.itemRequestToDto(any())).thenReturn(itemRequestDto);

        List<ItemRequestDto> result = itemRequestService.getAllItemRequestsByUser(1L);

        assertEquals(1, result.size());
        assertEquals(itemRequestDto, result.getFirst());
    }

    @Test
    void getAllItemRequestsShouldReturnSortedItemRequests() {

        when(itemRequestRepository.findAll()).thenReturn(List.of(itemRequest));
        when(itemRequestMapper.itemRequestToDto(itemRequest)).thenReturn(itemRequestDto);

        List<ItemRequestDto> result = itemRequestService.getAllItemRequests();

        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(itemRequestDto);
        verify(itemRequestRepository, times(1)).findAll();
        verify(itemRequestMapper, times(1)).itemRequestToDto(itemRequest);
    }

}