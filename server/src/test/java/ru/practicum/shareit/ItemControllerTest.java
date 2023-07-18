package ru.practicum.shareit;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.exception.BadRequestBookingException;
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.exception.BadRequestCommentException;
import ru.practicum.shareit.item.exception.BadRequestItemException;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.exception.UpdateWithoutXSharerException;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
public class ItemControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ItemService itemService;

    @SneakyThrows
    @Test
    void addItem() {
        Item itemToSave = new Item();
        long userId = 0;

        when(itemService.addItem(any(Item.class), anyLong())).thenReturn(itemToSave);

        String response = mvc.perform(post("/items")
                        .header("X-Sharer-User-Id", userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(itemToSave)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(itemToSave), response);
    }

    @SneakyThrows
    @Test
    void updateItem() {
        Item itemToUpdate = new Item();
        long userId = 0L;
        long itemId = 0L;

        mvc.perform(patch("/items/{itemId}", itemId)
                        .header("X-Sharer-User-Id", userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(itemToUpdate)))
                .andExpect(status().isOk());

        verify(itemService, times(1)).updateItem(itemToUpdate, userId, itemId);
    }

    @SneakyThrows
    @Test
    void getItemById() {
        long userId = 0L;
        long itemId = 0L;

        mvc.perform(get("/items/{itemId}", itemId)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk());

        verify(itemService, times(1)).getItemById(itemId, userId);
    }

    @SneakyThrows
    @Test
    void getUserItem() {
        long userId = 0L;
        Integer from = 0;
        Integer size = 32;

        mvc.perform(get("/items")
                        .header("X-Sharer-User-Id", userId)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isOk());

        verify(itemService, times(1)).getUserItem(userId, from, size);
    }

    @SneakyThrows
    @Test
    void searchItem() {
        String text = "test";
        Integer from = 0;
        Integer size = 32;
        mvc.perform(get("/items/search")
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size))
                        .param("text", text))
                .andExpect(status().isOk());

        verify(itemService, times(1)).searchItem(text, from, size);
    }

    @SneakyThrows
    @Test
    void addComment() {
        Comment commentToSave = new Comment();
        CommentDto comment = new CommentDto();
        long userId = 0L;
        long itemId = 0L;

        when(itemService.addComment(any(Comment.class), anyLong(), anyLong()))
                .thenReturn(comment);

        String response = mvc.perform(post("/items/{itemId}/comment", itemId)
                        .header("X-Sharer-User-Id", userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(commentToSave)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(comment), response);
    }

    @SneakyThrows
    @Test
    void addItemWithException() {
        Item itemToSave = new Item();
        long userId = 0;

        when(itemService.addItem(any(Item.class), anyLong()))
                .thenThrow(new BadRequestItemException("У предмета отсуствует доступность"));

        mvc.perform(post("/items")
                        .header("X-Sharer-User-Id", userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(itemToSave)))
                .andExpect(status().isBadRequest());

    }

    @SneakyThrows
    @Test
    void updateItemWithException() {
        Item itemToUpdate = new Item();
        long userId = 0L;
        long itemId = 0L;

        when(itemService.updateItem(any(Item.class), anyLong(), anyLong()))
                .thenThrow(new UpdateWithoutXSharerException("Запрос без заголовка"));

        mvc.perform(patch("/items/{itemId}", itemId)
                        .header("X-Sharer-User-Id", userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(itemToUpdate)))
                .andExpect(status().is5xxServerError());

    }

    @SneakyThrows
    @Test
    void getItemByIdWithException() {
        long userId = 0L;
        long itemId = 0L;

        when(itemService.getItemById(anyLong(), anyLong()))
                .thenThrow(new ItemNotFoundException("Предмет не найден"));

        mvc.perform(get("/items/{itemId}", itemId)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isNotFound());

    }

    @SneakyThrows
    @Test
    void addCommentWithCommentException() {
        Comment commentToSave = new Comment();
        CommentDto comment = new CommentDto();
        long userId = 0L;
        long itemId = 0L;

        when(itemService.addComment(any(Comment.class), anyLong(), anyLong()))
                .thenThrow(new BadRequestCommentException("У комментария отсуствует текст"));

        mvc.perform(post("/items/{itemId}/comment", itemId)
                        .header("X-Sharer-User-Id", userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(commentToSave)))
                .andExpect(status().isBadRequest());

    }

    @SneakyThrows
    @Test
    void addCommentWithBookingException() {
        Comment commentToSave = new Comment();
        CommentDto comment = new CommentDto();
        long userId = 0L;
        long itemId = 0L;

        when(itemService.addComment(any(Comment.class), anyLong(), anyLong()))
                .thenThrow(new BadRequestBookingException("У пользователя нет законченных бронирований"));

        mvc.perform(post("/items/{itemId}/comment", itemId)
                        .header("X-Sharer-User-Id", userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(commentToSave)))
                .andExpect(status().isBadRequest());

    }
}
