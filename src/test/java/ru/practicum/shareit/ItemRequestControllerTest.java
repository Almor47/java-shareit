package ru.practicum.shareit;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.controller.ItemRequestController;
import ru.practicum.shareit.request.exception.BadRequestItemRequestException;
import ru.practicum.shareit.request.exception.NotFoundItemRequestException;
import ru.practicum.shareit.request.exception.PaginationException;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.RequestService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
public class ItemRequestControllerTest {

    @MockBean
    private RequestService requestService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mvc;

    @SneakyThrows
    @Test
    void addRequest() {
        long userId = 0L;
        ItemRequest request = new ItemRequest();
        when(requestService.addRequest(any(ItemRequest.class), anyLong()))
                .thenReturn(request);

        String response = mvc.perform(post("/requests")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(request), response);
    }


    @SneakyThrows
    @Test
    void getOwnRequest() {
        long userId = 0L;

        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk());

        verify(requestService, times(1)).getOwnRequest(userId);
    }


    @SneakyThrows
    @Test
    void getOtherRequest() {
        long userId = 0L;
        int from = 0;
        int size = 32;

        mvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", userId)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isOk());

        verify(requestService, times(1)).getOtherRequest(userId, from, size);

    }

    @SneakyThrows
    @Test
    void getRequest() {
        long userId = 0L;
        long requestId = 0L;

        mvc.perform(get("/requests/{requestId}", requestId)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk());

        verify(requestService, times(1)).getRequest(userId, requestId);
    }

    @SneakyThrows
    @Test
    void addRequestWithException() {
        long userId = 0L;
        ItemRequest request = new ItemRequest();
        when(requestService.addRequest(any(ItemRequest.class), anyLong()))
                .thenThrow(new BadRequestItemRequestException("Запрос не может иметь пустое описание"));

        mvc.perform(post("/requests")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isBadRequest());

    }

    @SneakyThrows
    @Test
    void getRequestWithException() {
        long userId = 0L;
        long requestId = 0L;

        when(requestService.getRequest(anyLong(), anyLong()))
                .thenThrow(new NotFoundItemRequestException("Запрос не найден"));

        mvc.perform(get("/requests/{requestId}", requestId)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isNotFound());

    }

    @SneakyThrows
    @Test
    void getOtherRequestWithException() {
        long userId = 0L;
        int from = 0;
        int size = 32;

        when(requestService.getOtherRequest(anyLong(), anyInt(), anyInt()))
                .thenThrow(new PaginationException("Ошибка пагинации"));

        mvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", userId)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isBadRequest());

    }
}
