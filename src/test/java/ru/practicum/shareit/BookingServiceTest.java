package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.FullBookingDto;
import ru.practicum.shareit.booking.exception.BadRequestBookingException;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private ItemServiceImpl itemService;

    @Mock
    private UserServiceImpl userService;

    @InjectMocks
    private BookingServiceImpl bookingService;

    @Test
    void addBooking_whenAllValid_thenSaveBooking() {
        long userId = 0L;
        User user = User.builder()
                .name("Aleksandr")
                .email("test@yandex.ru")
                .build();

        Item item = Item.builder()
                .id(1L)
                .name("легкий сундук")
                .description("классный сундук")
                .available(true)
                .owner(10L)
                .build();

        BookingDto bookingDto = BookingDto.builder()
                .itemId(1L)
                .start(LocalDateTime.of(2023, 10, 10, 2, 2))
                .end(LocalDateTime.of(2023, 11, 11, 4, 4))
                .build();
        FullBookingDto fullBookingDto = new FullBookingDto();
        Booking booking = new Booking();

        when(itemService.getItemByIdRepository(anyLong()))
                .thenReturn(item);

        when(userService.getUserById(anyLong()))
                .thenReturn(user);

        when(bookingRepository.save(any(Booking.class)))
                .thenReturn(booking);

        FullBookingDto actualBooking = bookingService.addBooking(bookingDto, userId);

        assertEquals(bookingDto.getStart(), actualBooking.getStart());
        assertEquals(bookingDto.getEnd(), actualBooking.getEnd());
        assertEquals(bookingDto.getItemId(), actualBooking.getItem().getId());
    }

    @Test
    void addBooking_whenNotValid_thenThrowsException() {
        Long userId = 0L;
        BookingDto bookingDto = new BookingDto();
        Booking booking = new Booking();

        assertThrows(BadRequestBookingException.class, () ->
                bookingService.addBooking(bookingDto, userId));

        verify(bookingRepository, never()).save(booking);
    }
}
