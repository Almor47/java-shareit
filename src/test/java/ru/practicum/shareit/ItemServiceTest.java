package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exception.BadRequestCommentException;
import ru.practicum.shareit.item.exception.BadRequestItemException;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.exception.UpdateWithoutXSharerException;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserServiceImpl userService;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private ItemServiceImpl itemService;

    @Test
    void addItem_whenAllValid_thenSaveItem() {
        long userId = 0L;

        Item item = Item.builder()
                .name("сундук")
                .description("классный сундук")
                .available(true)
                .owner(userId)
                .build();


        when(itemRepository.save(item)).thenReturn(item);

        when(userService.getUserById(anyLong()))
                .thenReturn(new User());

        Item actualItem = itemService.addItem(item, userId);

        assertEquals(item, actualItem);
    }

    @Test
    void addItem_whenNotValid_thenThrowsException() {
        Item item = new Item();
        Long userId = 0L;

        assertThrows(BadRequestItemException.class, () ->
                itemService.addItem(item, userId));

        verify(itemRepository, never()).save(item);
    }

    @Test
    void addItem_whenNotValidUserId_thenThrowsException() {
        Item item = new Item();
        Long userId = null;

        assertThrows(UserNotFoundException.class, () ->
                itemService.addItem(item, userId));

        verify(itemRepository, never()).save(item);
    }

    @Test
    void addItem_whenNotValidName_thenThrowsException() {
        Item item = Item.builder()
                .name(null)
                .description("классный сундук")
                .available(true)
                .build();
        Long userId = 0L;

        assertThrows(BadRequestItemException.class, () ->
                itemService.addItem(item, userId));

        verify(itemRepository, never()).save(item);
    }

    @Test
    void addItem_whenNotValidDescription_thenThrowsException() {
        Item item = Item.builder()
                .name("сундук")
                .description(null)
                .available(true)
                .build();
        Long userId = 0L;

        assertThrows(BadRequestItemException.class, () ->
                itemService.addItem(item, userId));

        verify(itemRepository, never()).save(item);
    }

    @Test
    void addItem_whenNotValidUser_thenThrowsException() {
        Item item = Item.builder()
                .name("сундук")
                .description("Классный сундук")
                .available(true)
                .build();
        Long userId = 0L;

        when(userService.getUserById(anyLong()))
                .thenReturn(null);

        assertThrows(UserNotFoundException.class, () ->
                itemService.addItem(item, userId));

        verify(itemRepository, never()).save(item);
    }


    @Test
    void getItemById_whenItemFound_thenReturnItem() {
        long itemId = 0L;
        long userId = 0L;
        Item expectedItem = Item.builder()
                .name("сундук")
                .description("классный сундук")
                .available(true)
                .owner(userId)
                .comment(new ArrayList<>())
                .build();
        List<Booking> booking = new ArrayList<>();

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(expectedItem));
        when(bookingRepository.findLastBooking(anyLong(), any(LocalDateTime.class)))
                .thenReturn(booking);
        when(bookingRepository.findNextBooking(anyLong(), any(LocalDateTime.class)))
                .thenReturn(booking);

        ItemDto actualItem = itemService.getItemById(itemId, userId);

        assertThat(actualItem.getId(), equalTo(expectedItem.getId()));
        assertThat(actualItem.getName(), equalTo(expectedItem.getName()));
        assertThat(actualItem.getDescription(), equalTo(expectedItem.getDescription()));
        assertThat(actualItem.getAvailable(), equalTo(expectedItem.getAvailable()));
        assertThat(actualItem.getOwner(), equalTo(expectedItem.getOwner()));

    }

    @Test
    void getItemById_whenItemNotFound_thenReturnNotFoundException() {
        long itemId = 0L;
        long userId = 0L;

        when(itemRepository.findById(itemId)).thenThrow(
                new ItemNotFoundException("Предмет с id " + itemId + "не найден"));

        assertThrows(ItemNotFoundException.class,
                () -> itemService.getItemById(itemId, userId));

    }

    @Test
    void updateItem_whenUserIdNull_thenReturnUpdateWithoutXSharerException() {
        Long userId = null;
        long itemId = 0L;

        Item item = Item.builder()
                .name("сундук")
                .description("классный сундук")
                .available(true)
                .build();

        assertThrows(UpdateWithoutXSharerException.class, () ->
                itemService.updateItem(item,userId,itemId));

        verify(itemRepository, never()).save(item);
    }

    @Test
    void updateItem_whenUserNotFound_thenThrowException() {
        long userId = 0L;
        long itemId = 0L;

        Item item = Item.builder()
                .name("сундук")
                .description("классный сундук")
                .available(true)
                .build();

        when(userService.getUserById(userId)).thenReturn(null);

        assertThrows(UserNotFoundException.class, () ->
                itemService.updateItem(item,userId,itemId));

        verify(itemRepository, never()).save(item);
    }

    @Test
    void addComment_whenCommentEmpty_thenReturnBadRequestCommentException() {
        long userId = 0L;
        long itemId = 0L;

        Comment comment = new Comment();

        assertThrows(BadRequestCommentException.class, () ->
                itemService.addComment(comment,userId,itemId));

        verify(commentRepository, never()).save(comment);
    }
}
