package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
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

@Transactional
@DataJpaTest
public class BookingRepositoryTest {
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void findAllBookingByOwnerId() {

        User userToSave = User.builder()
                .name("Aleksandr")
                .email("test@yandex.ru")
                .build();

        User saveUser = userRepository.save(userToSave);

        User userToSave2 = User.builder()
                .name("Booker")
                .email("booker@yandex.ru")
                .build();

        User saveUser2 = userRepository.save(userToSave2);


        Item itemToSave1 = Item.builder()
                .name("Пила")
                .description("Острая пила")
                .available(true)
                .owner(saveUser.getId())
                .requestId(0L)
                .build();

        Item saveItem1 = itemRepository.save(itemToSave1);


        Item itemToSave2 = Item.builder()
                .name("Ручка")
                .description("Тяжелая ручка")
                .available(true)
                .owner(saveUser.getId())
                .requestId(0L)
                .build();

        Item saveItem2 = itemRepository.save(itemToSave2);


        Item itemToSave3 = Item.builder()
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

        long user = saveUser.getId();
        String state = "ALL";
        Pageable page = PageRequest.of(0, 32);

        List<Booking> expectedBooking = bookingRepository.findAllByOwnerId(user, state, LocalDateTime.now(), page);

        assertEquals(5, expectedBooking.size());
    }

    @Test
    void findLastBooking() {
        User userToSave = User.builder()
                .name("Aleksandr")
                .email("test@yandex.ru")
                .build();

        User saveUser = userRepository.save(userToSave);

        User userToSave2 = User.builder()
                .name("Booker")
                .email("booker@yandex.ru")
                .build();

        User saveUser2 = userRepository.save(userToSave2);


        Item itemToSave1 = Item.builder()
                .name("Пила")
                .description("Острая пила")
                .available(true)
                .owner(saveUser.getId())
                .requestId(0L)
                .build();

        Item saveItem1 = itemRepository.save(itemToSave1);


        Item itemToSave2 = Item.builder()
                .name("Ручка")
                .description("Тяжелая ручка")
                .available(true)
                .owner(saveUser.getId())
                .requestId(0L)
                .build();

        Item saveItem2 = itemRepository.save(itemToSave2);


        Item itemToSave3 = Item.builder()
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

        long itemId = itemToSave1.getId();

        List<Booking> expectedBooking = bookingRepository.findLastBooking(itemId, LocalDateTime.now());

        assertEquals(1, expectedBooking.size());
    }

    @Test
    void findLastBooking_whenStatusRejected_thenReturnEmptyList() {
        User userToSave = User.builder()
                .name("Aleksandr")
                .email("test@yandex.ru")
                .build();

        User saveUser = userRepository.save(userToSave);

        User userToSave2 = User.builder()
                .name("Booker")
                .email("booker@yandex.ru")
                .build();

        User saveUser2 = userRepository.save(userToSave2);


        Item itemToSave1 = Item.builder()
                .name("Пила")
                .description("Острая пила")
                .available(true)
                .owner(saveUser.getId())
                .requestId(0L)
                .build();

        Item saveItem1 = itemRepository.save(itemToSave1);


        Item itemToSave2 = Item.builder()
                .name("Ручка")
                .description("Тяжелая ручка")
                .available(true)
                .owner(saveUser.getId())
                .requestId(0L)
                .build();

        Item saveItem2 = itemRepository.save(itemToSave2);


        Item itemToSave3 = Item.builder()
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

        long itemId = itemToSave2.getId();

        List<Booking> expectedBooking = bookingRepository.findLastBooking(itemId, LocalDateTime.now());

        assertEquals(0, expectedBooking.size());
    }

    @Test
    void findNextBooking_whenStatusRejected_thenReturnEmptyList() {
        User userToSave = User.builder()
                .name("Aleksandr")
                .email("test@yandex.ru")
                .build();

        User saveUser = userRepository.save(userToSave);

        User userToSave2 = User.builder()
                .name("Booker")
                .email("booker@yandex.ru")
                .build();

        User saveUser2 = userRepository.save(userToSave2);


        Item itemToSave1 = Item.builder()
                .name("Пила")
                .description("Острая пила")
                .available(true)
                .owner(saveUser.getId())
                .requestId(0L)
                .build();

        Item saveItem1 = itemRepository.save(itemToSave1);


        Item itemToSave2 = Item.builder()
                .name("Ручка")
                .description("Тяжелая ручка")
                .available(true)
                .owner(saveUser.getId())
                .requestId(0L)
                .build();

        Item saveItem2 = itemRepository.save(itemToSave2);


        Item itemToSave3 = Item.builder()
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

        long itemId = itemToSave2.getId();

        List<Booking> expectedBooking = bookingRepository.findLastBooking(itemId, LocalDateTime.now());

        assertEquals(0, expectedBooking.size());
    }

}
