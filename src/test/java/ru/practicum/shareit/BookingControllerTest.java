package ru.practicum.shareit;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.FullBookingDto;
import ru.practicum.shareit.booking.service.BookingService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
public class BookingControllerTest {

    @MockBean
    private BookingService bookingService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mvc;


    @SneakyThrows
    @Test
    void addBooking() {
        FullBookingDto fullBookingDto = new FullBookingDto();
        BookingDto bookingDto = new BookingDto();
        long userId = 0L;

        when(bookingService.addBooking(any(BookingDto.class), anyLong()))
                .thenReturn(fullBookingDto);

        String response = mvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(bookingDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(fullBookingDto), response);
    }

    @SneakyThrows
    @Test
    void updateBooking() {
        long bookingId = 0L;
        long userId = 0L;
        boolean approved = true;

        mvc.perform(patch("/bookings/{bookingId}", bookingId)
                        .header("X-Sharer-User-Id", userId)
                        .param("approved", String.valueOf(approved)))
                .andExpect(status().isOk());

        verify(bookingService, times(1)).updateBooking(bookingId, approved, userId);
    }

    @SneakyThrows
    @Test
    void getBookingById() {
        long bookingId = 0L;
        long userId = 0L;

        mvc.perform(get("/bookings/{bookingId}", bookingId)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk());

        verify(bookingService, times(1)).getBookingById(bookingId, userId);
    }

    @SneakyThrows
    @Test
    void getAllUserBooking() {
        String state = "ALL";
        long userId = 0L;
        Integer from = 0;
        Integer size = 32;

        mvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", userId)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size))
                        .param("state", state))
                .andExpect(status().isOk());

        verify(bookingService, times(1)).getAllUserBooking(state, userId, from, size);
    }

    @SneakyThrows
    @Test
    void getAllItemUserBooking() {
        String state = "ALL";
        long userId = 0L;
        Integer from = 0;
        Integer size = 32;

        mvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", userId)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size))
                        .param("state", state))
                .andExpect(status().isOk());

        verify(bookingService, times(1)).getAllItemUserBooking(state, userId, from, size);
    }


}
