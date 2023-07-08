package ru.practicum.shareit;


import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.FullBookingDto;
import ru.practicum.shareit.booking.enumerated.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;


@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingServiceImplIntegrationTest {

    private final EntityManager entityManager;

    private final BookingServiceImpl bookingService;

    @Test
    void addBooking() {
        User userToSave1 = User.builder()
                .name("Aleksandr")
                .email("test@yandex.ru")
                .build();
        entityManager.persist(userToSave1);

        User userToSave2 = User.builder()
                .name("Booker")
                .email("booker@yandex.ru")
                .build();
        entityManager.persist(userToSave2);

        Item itemToSave = Item.builder()
                .name("сундук")
                .description("классный сундук")
                .available(true)
                .owner(userToSave1.getId())
                .build();
        entityManager.persist(itemToSave);

        BookingDto bookingDtoToSave = BookingDto.builder()
                .start(LocalDateTime.of(2023, 10, 10, 2, 2))
                .end(LocalDateTime.of(2023, 11, 11, 3, 3))
                .itemId(itemToSave.getId())
                .build();
        FullBookingDto expectedBooking = bookingService.addBooking(bookingDtoToSave, userToSave2.getId());

        assertThat(expectedBooking.getId(), notNullValue());
        assertThat(expectedBooking.getStart(), equalTo(bookingDtoToSave.getStart()));
        assertThat(expectedBooking.getEnd(), equalTo(bookingDtoToSave.getEnd()));
        assertThat(expectedBooking.getBooker().getId(), equalTo(userToSave2.getId()));
        assertThat(expectedBooking.getItem().getId(), equalTo(bookingDtoToSave.getItemId()));

    }

    @Test
    void updateBooking() {
        User userToSave1 = User.builder()
                .name("Aleksandr")
                .email("test@yandex.ru")
                .build();
        entityManager.persist(userToSave1);

        User userToSave2 = User.builder()
                .name("Booker")
                .email("booker@yandex.ru")
                .build();
        entityManager.persist(userToSave2);

        Item itemToSave = Item.builder()
                .name("сундук")
                .description("классный сундук")
                .available(true)
                .owner(userToSave1.getId())
                .user(userToSave1)
                .build();
        entityManager.persist(itemToSave);

        Booking booking = Booking.builder()
                .start(LocalDateTime.of(2023, 10, 10, 2, 2))
                .end(LocalDateTime.of(2023, 11, 11, 3, 3))
                .itemId(itemToSave.getId())
                .bookerId(userToSave2.getId())
                .build();
        entityManager.persist(booking);

        FullBookingDto expectedBooking = bookingService.updateBooking(booking.getId(), true, userToSave1.getId());

        assertThat(expectedBooking.getId(), notNullValue());
        assertThat(expectedBooking.getStatus(), equalTo(Status.APPROVED));


    }

    @Test
    void getBookingById() {
        User userToSave1 = User.builder()
                .name("Aleksandr")
                .email("test@yandex.ru")
                .build();
        entityManager.persist(userToSave1);

        User userToSave2 = User.builder()
                .name("Booker")
                .email("booker@yandex.ru")
                .build();
        entityManager.persist(userToSave2);

        Item itemToSave = Item.builder()
                .name("сундук")
                .description("классный сундук")
                .available(true)
                .owner(userToSave1.getId())
                .user(userToSave1)
                .build();
        entityManager.persist(itemToSave);

        Booking booking = Booking.builder()
                .start(LocalDateTime.of(2023, 10, 10, 2, 2))
                .end(LocalDateTime.of(2023, 11, 11, 3, 3))
                .itemId(itemToSave.getId())
                .bookerId(userToSave2.getId())
                .build();
        entityManager.persist(booking);

        FullBookingDto expectedBooking = bookingService.getBookingById(booking.getId(), userToSave2.getId());

        assertThat(expectedBooking.getId(), notNullValue());
        assertThat(expectedBooking.getStart(), equalTo(booking.getStart()));
        assertThat(expectedBooking.getEnd(), equalTo(booking.getEnd()));
        assertThat(expectedBooking.getBooker().getId(),
                equalTo(booking.getBookerId()));
        assertThat(expectedBooking.getItem().getId(), equalTo(booking.getItemId()));


    }

    @Test
    void getAllUserBooking() {
        User userToSave1 = User.builder()
                .name("Aleksandr")
                .email("test@yandex.ru")
                .build();
        entityManager.persist(userToSave1);

        User userToSave2 = User.builder()
                .name("Booker")
                .email("booker@yandex.ru")
                .build();
        entityManager.persist(userToSave2);

        Item itemToSave = Item.builder()
                .name("сундук")
                .description("классный сундук")
                .available(true)
                .owner(userToSave1.getId())
                .user(userToSave1)
                .build();
        entityManager.persist(itemToSave);

        Booking booking1 = Booking.builder()
                .start(LocalDateTime.of(2023, 10, 10, 2, 2))
                .end(LocalDateTime.of(2023, 11, 11, 3, 3))
                .itemId(itemToSave.getId())
                .bookerId(userToSave2.getId())
                .build();
        entityManager.persist(booking1);

        Booking booking2 = Booking.builder()
                .start(LocalDateTime.of(2023, 10, 10, 2, 2))
                .end(LocalDateTime.of(2023, 11, 11, 3, 3))
                .itemId(itemToSave.getId())
                .bookerId(userToSave2.getId())
                .build();
        entityManager.persist(booking2);

        List<FullBookingDto> expectedBookings =
                bookingService.getAllUserBooking("ALL", userToSave2.getId(), 0, 32);

        assertThat(expectedBookings.size(), equalTo(2));
    }

    @Test
    void getAllItemUserBooking() {
        User userToSave1 = User.builder()
                .name("Aleksandr")
                .email("test@yandex.ru")
                .build();
        entityManager.persist(userToSave1);

        User userToSave2 = User.builder()
                .name("Booker")
                .email("booker@yandex.ru")
                .build();
        entityManager.persist(userToSave2);

        Item itemToSave = Item.builder()
                .name("сундук")
                .description("классный сундук")
                .available(true)
                .owner(userToSave1.getId())
                .user(userToSave1)
                .build();
        entityManager.persist(itemToSave);

        Booking booking1 = Booking.builder()
                .start(LocalDateTime.of(2023, 10, 10, 2, 2))
                .end(LocalDateTime.of(2023, 11, 11, 3, 3))
                .itemId(itemToSave.getId())
                .bookerId(userToSave2.getId())
                .build();
        entityManager.persist(booking1);

        List<FullBookingDto> expectedBookings =
                bookingService.getAllUserBooking("ALL", userToSave2.getId(), 0, 32);

        assertThat(expectedBookings.size(), equalTo(1));
    }
}
