package ru.practicum.shareit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.enumerated.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class BookingRepositoryTest {
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void createBookingForTest() {

        User userToSave = User.builder()
                .id(1L)
                .name("Aleksandr")
                .email("test@yandex.ru")
                .build();

        User saveUser = userRepository.save(userToSave);

        User userToSave2 = User.builder()
                .id(2L)
                .name("Booker")
                .email("booker@yandex.ru")
                .build();

        User saveUser2 = userRepository.save(userToSave2);

        Item itemToSave1 = Item.builder()
                .id(1L)
                .name("Пила")
                .description("Острая пила")
                .available(true)
                .owner(saveUser.getId())
                .requestId(0L)
                .build();

        Item saveItem1 = itemRepository.save(itemToSave1);

        Item itemToSave2 = Item.builder()
                .id(2L)
                .name("Ручка")
                .description("Тяжелая ручка")
                .available(true)
                .owner(saveUser.getId())
                .requestId(0L)
                .build();

        Item saveItem2 = itemRepository.save(itemToSave2);

        Item itemToSave3 = Item.builder()
                .id(3L)
                .name("Дрель")
                .description("Много сверел")
                .available(true)
                .owner(saveUser.getId())
                .requestId(0L)
                .build();

        Item saveItem3 = itemRepository.save(itemToSave3);


        Booking bookingToSave1 = Booking.builder()
                .status(Status.APPROVED)
                .start(LocalDateTime.of(2022, 10, 10, 1, 1))
                .end(LocalDateTime.of(2022, 10, 10, 2, 2))
                .itemId(saveItem1.getId())
                .bookerId(saveUser2.getId())
                .build();

        Booking bookingToSave2 = Booking.builder()
                .status(Status.APPROVED)
                .start(LocalDateTime.of(2023, 6, 10, 3, 3))
                .end(LocalDateTime.of(2023, 10, 10, 4, 4))
                .itemId(saveItem3.getId())
                .bookerId(saveUser2.getId())
                .build();

        Booking bookingToSave3 = Booking.builder()
                .status(Status.REJECTED)
                .start(LocalDateTime.of(2022, 9, 9, 3, 3))
                .end(LocalDateTime.of(2022, 9, 9, 4, 4))
                .itemId(saveItem2.getId())
                .bookerId(saveUser2.getId())
                .build();

        Booking bookingToSave4 = Booking.builder()
                .status(Status.APPROVED)
                .start(LocalDateTime.of(2023, 10, 10, 1, 1))
                .end(LocalDateTime.of(2023, 10, 11, 2, 2))
                .itemId(saveItem1.getId())
                .bookerId(saveUser2.getId())
                .build();

        Booking bookingToSave5 = Booking.builder()
                .status(Status.APPROVED)
                .start(LocalDateTime.of(2023, 8, 20, 1, 1))
                .end(LocalDateTime.of(2023, 8, 20, 2, 2))
                .itemId(saveItem1.getId())
                .bookerId(saveUser2.getId())
                .build();

        bookingRepository.save(bookingToSave1);
        bookingRepository.save(bookingToSave2);
        bookingRepository.save(bookingToSave3);
        bookingRepository.save(bookingToSave4);
        bookingRepository.save(bookingToSave5);
    }

    @Test
    void findAllBookingByOwnerId() {
        long booker = 1L;
        String state = "CURRENT";
        Pageable page = PageRequest.of(0, 32);

        List<Booking> expectedBooking = bookingRepository.findAllByOwnerId(booker, state, LocalDateTime.now(), page);

        assertEquals(5, expectedBooking.size());
    }

    @Test
    void findLastBooking() {
        long itemId = 1L;
        List<Booking> expectedBooking = bookingRepository.findLastBooking(itemId, LocalDateTime.now());

        assertEquals(1, expectedBooking.size());
    }

    @Test
    void findLastBooking_whenStatusRejected_thenReturnEmptyList() {
        long itemId = 2L;
        List<Booking> expectedBooking = bookingRepository.findLastBooking(itemId, LocalDateTime.now());

        assertEquals(0, expectedBooking.size());
    }

    @Test
    void findNextBooking_whenStatusRejected_thenReturnEmptyList() {
        long itemId = 2L;
        List<Booking> expectedBooking = bookingRepository.findLastBooking(itemId, LocalDateTime.now());

        assertEquals(0, expectedBooking.size());
    }


    @AfterEach
    void deleteAllBooking() {
        bookingRepository.deleteAll();
    }
}
