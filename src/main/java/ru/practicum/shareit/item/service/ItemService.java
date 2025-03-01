package ru.practicum.shareit.item.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.enums.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.ConditionsNotMetException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comments;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.CommentsRepository;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemMapper itemMapper;
    private final CommentsMapper commentsMapper;
    private final BookingService bookingService;
    private final CommentsRepository commentsRepository;
    private final BookingMapper bookingMapper;

    public CommentsDto createComments(Long userId, Long itemId, CommentsDto commentDto) {

        User author = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %d not found", userId)));

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(String.format("Item with id %d not found", itemId)));

        bookingService.getAllBookingsByUser(userId, "ALL").stream()
                .filter(booking -> Objects.equals(booking.getBooker().getId(), userId) &&
                        (LocalDateTime.now().isAfter(booking.getEnd()) &&
                                booking.getStatus() == BookingStatus.APPROVED))
                .findAny()
                .orElseThrow(() -> new ConditionsNotMetException("You can not comment this item"));

        return commentsMapper.toCommentsDto(commentsRepository
                .save(commentsMapper.toCommentsEntity(commentDto, item, author)));
    }

    public ItemFullDto getItemById(Long userId, Long itemId) {

        Item item = itemRepository.findByIdInFull(itemId).orElseThrow(() ->
                new NotFoundException(String.format("Item with id %d not found", itemId)));

        ItemFullDto itemFullDto = itemMapper.toItemFullDto(item);

        setComments(itemFullDto, item.getComments());

        if (Objects.equals(itemFullDto.getOwnerId(), userId)) {
            setBookings(itemFullDto, item.getBookings());
        }

        return itemFullDto;
    }

    private void setComments(ItemFullDto itemFullDto, Set<Comments> comments) {
        itemFullDto.setComments(comments.stream()
                .map(commentsMapper::toCommentsDto)
                .collect(Collectors.toSet()));
    }

    private void setBookings(ItemFullDto itemFullDto, Set<Booking> bookings) {

        itemFullDto.setLastBooking(
                bookings.stream()
                        .filter(booking -> booking.getStatus() != BookingStatus.REJECTED)
                        .filter(booking -> booking.getEnd().isBefore(LocalDateTime.now()))
                        .max(Comparator.comparing(Booking::getEnd))
                        .map(bookingMapper::toBookingDto)
                        .orElse(null)
        );

        itemFullDto.setNextBooking(
                bookings.stream()
                        .filter(booking -> booking.getStatus() != BookingStatus.REJECTED)
                        .filter(booking -> booking.getStart().isAfter(LocalDateTime.now()))
                        .min(Comparator.comparing(Booking::getStart))
                        .map(bookingMapper::toBookingDto)
                        .orElse(null)
        );
    }

    public ItemDto addItem(Item item) {
        Item addedItem = itemRepository.save(item);
        return itemMapper.toItemDto(addedItem);
    }

    @Transactional
    public ItemDto updateItem(Item item) {
        Item existingItem = itemRepository.findById(item.getId())
                .orElseThrow(() -> new NotFoundException(String.format("Item with id %d not found", item.getId())));

        if (item.getName() != null) {
            existingItem.setName(item.getName());
        }
        if (item.getDescription() != null) {
            existingItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            existingItem.setAvailable(item.getAvailable());
        }

        Item updatedItem = itemRepository.save(existingItem);
        return itemMapper.toItemDto(updatedItem);
    }

    public void deleteItem(long id) {
        Item existingItem = itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Item with id %d not found", id)));
        itemRepository.deleteById(id);
    }

    public Collection<ItemDto> getAllItemsByOwner(Long userId) {
        return itemRepository.findAll().stream()
                .filter(item -> item.getOwnerId() != null && item.getOwnerId().equals(userId))
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    public Collection<ItemDto> searchItemsByQuery(String query) {
        if (query == null || query.isEmpty()) {
            log.debug("Request field is empty");
            return Collections.emptyList();
        }

        List<Item> items = itemRepository.findByNameContainingIgnoreCase(query);
        log.debug("Found {} items for query: {}", items.size(), query);
        return items.stream()
                .filter(item -> item.getAvailable() != null && item.getAvailable())
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    public ItemDto findItemById(Long id) {
        return itemMapper.toItemDto(itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Item with id %d not found", id))));
    }
}
