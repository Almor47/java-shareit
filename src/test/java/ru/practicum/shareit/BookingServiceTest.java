package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.FullBookingDto;
import ru.practicum.shareit.booking.exception.BadRequestBookingException;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private BookingService bookingService ;

    /*@Test
    void addBooking_whenAllValid_thenSaveBooking() {
        Booking booking = new Booking();
        FullBookingDto fullBookingDto = new FullBookingDto();
        // через билдер задать поля чтобы пройти валидацию
        Long userId = 0L;
        // через билдер задать поля чтобы пройти валидацию
        when(bookingRepository.save(booking)).thenReturn(booking);

        FullBookingDto actualBooking = bookingService.addBooking(bookingDto, userId);

        assertEquals(fullBookingDto, actualBooking);
    }

    @Test
    void addItem_whenNotValid_thenThrowsException() {
        BookingDto bookingDto = new BookingDto();
        Long userId = 0L;
        // через билдер задать поля чтобы пройти валидацию

        assertThrows(BadRequestBookingException.class, () ->
                bookingService.addBooking(bookingDto, userId));

        verify(bookingRepository, never()).save(bookingDto);
    }*/
}
