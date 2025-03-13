package ru.practicum.shareit.server.item.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.server.booking.dto.BookingFullDto;
import ru.practicum.shareit.server.booking.mapper.BookingMapper;
import ru.practicum.shareit.server.booking.model.Booking;
import ru.practicum.shareit.server.booking.model.enums.BookingStatus;
import ru.practicum.shareit.server.booking.service.BookingService;
import ru.practicum.shareit.server.exception.ConditionsNotMetException;
import ru.practicum.shareit.server.item.dto.CommentsDto;
import ru.practicum.shareit.server.item.dto.ItemDto;
import ru.practicum.shareit.server.item.dto.ItemFullDto;
import ru.practicum.shareit.server.item.mapper.CommentsMapper;
import ru.practicum.shareit.server.item.mapper.ItemMapper;
import ru.practicum.shareit.server.item.model.Comments;
import ru.practicum.shareit.server.item.model.Item;
import ru.practicum.shareit.server.item.storage.CommentsRepository;
import ru.practicum.shareit.server.item.storage.ItemRepository;
import ru.practicum.shareit.server.request.storage.ItemRequestRepository;
import ru.practicum.shareit.server.user.dto.UserDto;
import ru.practicum.shareit.server.user.model.User;
import ru.practicum.shareit.server.user.storage.UserRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRequestRepository itemRequestRepository;

    @Mock
    private ItemMapper itemMapper;

    @Mock
    private CommentsMapper commentsMapper;

    @Mock
    private BookingService bookingService;

    @Mock
    private CommentsRepository commentsRepository;

    @Mock
    private BookingMapper bookingMapper;

    @InjectMocks
    private ItemService itemService;

    private User user;
    private Item item;
    private CommentsDto commentsDto;
    private Comments comments;
    private ItemDto itemDto;
    private ItemFullDto itemFullDto;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        userDto = new UserDto();
        user = new User();
        user.setId(2L);

        item = new Item();
        item.setId(1L);
        item.setAvailable(true);
        item.setOwnerId(1L);
        Comments comment = new Comments();
        comment.setId(1L);
        comment.setText("Это комментарий");

        Set<Comments> commentsSet = new HashSet<>();
        commentsSet.add(comment);

        Set<Booking> bookings = new HashSet<>();
        item.setBookings(bookings);

        item.setComments(commentsSet);

        commentsDto = new CommentsDto();
        commentsDto.setText("Nice item!");
        commentsDto.setCreated(Timestamp.valueOf(LocalDateTime.now()));

        comments = new Comments();
        comments.setId(1L);
        comments.setText("Nice item!");
        comments.setCreated(Timestamp.valueOf(LocalDateTime.now()));

        itemDto = new ItemDto();
        itemDto.setName("Item Name");

        itemFullDto = new ItemFullDto();
        itemFullDto.setId(1L);
        itemFullDto.setOwnerId(1L);
        itemFullDto.setComments(Set.of(commentsDto));
    }

    @Test
    public void createComments_ShouldReturnCommentsDto_WhenValidInput() {

        when(userRepository.findById(2L)).thenReturn(Optional.of(user));

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        BookingFullDto bookingFullDto = new BookingFullDto();
        bookingFullDto.setStart(LocalDateTime.now().minusDays(10));
        bookingFullDto.setEnd(LocalDateTime.now().minusDays(5));
        bookingFullDto.setBooker(new UserDto(2L, "Test user", "Test@mail.ru"));
        bookingFullDto.setStatus(BookingStatus.APPROVED);
        when(bookingService.getAllBookingsByUser(2L, "ALL")).thenReturn(Collections.singletonList(bookingFullDto));
        when(bookingService.getAllBookingsByUser(2L, "ALL")).thenReturn(Collections.singletonList(bookingFullDto));

        when(commentsMapper.toCommentsEntity(commentsDto, item, user)).thenReturn(comments);
        when(commentsRepository.save(comments)).thenReturn(comments);
        when(commentsMapper.toCommentsDto(comments)).thenReturn(commentsDto);

        CommentsDto result = itemService.createComments(2L, 1L, commentsDto);

        assertNotNull(result);
        assertEquals(commentsDto.getText(), result.getText());
        verify(commentsRepository, times(1)).save(any());
    }

    @Test
    public void createComments_ShouldThrowConditionsNotMetException_WhenNoApprovedBooking() {

        when(userRepository.findById(2L)).thenReturn(Optional.of(user));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        when(bookingService.getAllBookingsByUser(2L, "ALL")).thenReturn(Collections.emptyList());

        assertThatThrownBy(() -> {
            itemService.createComments(2L, 1L, commentsDto);
        }).isInstanceOf(ConditionsNotMetException.class)
                .hasMessage("You can not comment this item");

        verify(commentsRepository, never()).save(any());
    }

    @Test
    public void searchItemsByQuery_ShouldReturnEmptyList_WhenQueryIsNull() {
        Collection<ItemDto> result = itemService.searchItemsByQuery(null);

        assertThat(result).isEmpty();
    }

    @Test
    public void searchItemsByQuery_ShouldReturnEmptyList_WhenQueryIsEmpty() {
        Collection<ItemDto> result = itemService.searchItemsByQuery("");

        assertThat(result).isEmpty();
    }

    @Test
    public void searchItemsByQuery_ShouldReturnEmptyList_WhenQueryIsWhitespace() {
        Collection<ItemDto> result = itemService.searchItemsByQuery("   ");

        assertThat(result).isEmpty();
    }

    @Test
    public void getItemById_ShouldReturnItemFullDto_WhenItemExists() {
        when(itemRepository.findByIdInFull(1L)).thenReturn(Optional.of(item));
        when(itemMapper.toItemFullDto(item)).thenReturn(itemFullDto);

        ItemFullDto result = itemService.getItemById(1L, 1L);

        assertNotNull(result);
        assertEquals(itemFullDto.getId(), result.getId());
    }

    @Test
    public void addItem_ShouldReturnItemDto_WhenValidInput() {
        when(itemMapper.toItemEntity(itemDto, 1L, null)).thenReturn(item);
        when(itemRepository.save(item)).thenReturn(item);
        when(itemMapper.toItemDto(item)).thenReturn(itemDto);

        ItemDto result = itemService.addItem(1L, itemDto);

        assertNotNull(result);
        assertEquals(itemDto.getName(), result.getName());
        verify(itemRepository, times(1)).save(any());
    }

    @Test
    public void updateItem_ShouldReturnUpdatedItemDto_WhenItemExists() {
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(itemRepository.save(item)).thenReturn(item);
        when(itemMapper.toItemDto(item)).thenReturn(itemDto);

        ItemDto result = itemService.updateItem(item);

        assertNotNull(result);
        assertEquals(itemDto.getName(), result.getName());
        verify(itemRepository, times(1)).save(any());
    }

    @Test
    void searchItemsByQuery_ShouldReturnFilteredItems() {

        when(itemRepository.findByNameContainingIgnoreCase("item")).thenReturn(List.of(item));
        when(itemMapper.toItemDto(item)).thenReturn(itemDto);

        Collection<ItemDto> result = itemService.searchItemsByQuery("item");

        assertThat(result).hasSize(1);
        assertThat(result.iterator().next()).isEqualTo(itemDto);
        verify(itemRepository, times(1)).findByNameContainingIgnoreCase("item");
    }

    @Test
    void findItemById_ShouldReturnItemDtoWhenItemExists() {

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(itemMapper.toItemDto(item)).thenReturn(itemDto);

        ItemDto result = itemService.findItemById(1L);

        assertThat(result).isEqualTo(itemDto);
        verify(itemRepository, times(1)).findById(1L);
    }

    @Test
    void deleteItem_ShouldDeleteExistingItem() {

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        itemService.deleteItem(1L);

        verify(itemRepository, times(1)).deleteById(1L);
    }

    @Test
    void getAllItemsByOwner_ShouldReturnItemsForOwner() {

        item.setOwnerId(2L);
        when(itemRepository.findAll()).thenReturn(List.of(item));
        when(itemMapper.toItemDto(item)).thenReturn(itemDto);

        Collection<ItemDto> result = itemService.getAllItemsByOwner(2L);

        assertThat(result).hasSize(1);
        assertThat(result.iterator().next()).isEqualTo(itemDto);
        verify(itemRepository, times(1)).findAll();
    }

    @Test
    void getItemsByRequestId_ShouldReturnItemDtoList() {
        Long requestId = 1L;

        when(itemRepository.findByRequestId(requestId)).thenReturn(List.of(item));

        when(itemMapper.toItemDto(item)).thenReturn(itemDto);

        List<ItemDto> result = itemService.getItemsByRequestId(requestId);

        assertEquals(1, result.size(), "Should return one item");
        assertEquals(itemDto, result.get(0), "Returned itemDto should match the expected itemDto");
    }
}

