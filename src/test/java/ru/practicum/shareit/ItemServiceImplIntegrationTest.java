package ru.practicum.shareit;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.enumerated.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.user.model.User;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceImplIntegrationTest {

    private final ItemServiceImpl itemService;

    private final EntityManager entityManager;


    @Test
    void addItem() {

        User userToSave = User.builder()
                .name("Aleksandr")
                .email("test@yandex.ru")
                .build();
        entityManager.persist(userToSave);

        Item itemToSave = Item.builder()
                .name("сундук")
                .description("классный сундук")
                .available(true)
                .owner(userToSave.getId())
                .build();
        itemService.addItem(itemToSave, userToSave.getId());

        val query = entityManager.createQuery(
                "select i from Item i where i.name = :name", Item.class);
        Item item = query.setParameter("name",itemToSave.getName()).getSingleResult();

        assertThat(item.getId(), notNullValue());
        assertThat(item.getName(),equalTo(itemToSave.getName()));
        assertThat(item.getDescription(),equalTo(itemToSave.getDescription()));
        assertThat(item.getAvailable(),equalTo(itemToSave.getAvailable()));
        assertThat(item.getOwner(),equalTo(itemToSave.getOwner()));

    }

    @Test
    void updateItem() {
        User userToSave = User.builder()
                .name("Aleksandr")
                .email("test@yandex.ru")
                .build();
        entityManager.persist(userToSave);

        Item itemToSave = Item.builder()
                .name("сундук")
                .description("классный сундук")
                .available(true)
                .owner(userToSave.getId())
                .build();
        entityManager.persist(itemToSave);

        Item itemToUpdate = Item.builder()
                .name("сундук")
                .description("классный сундук с прочной ручкой")
                .available(true)
                .owner(userToSave.getId())
                .build();

        Item targetItem = itemService.updateItem(itemToUpdate,
                userToSave.getId(), itemToSave.getId());

        assertThat(targetItem.getId(), equalTo(itemToSave.getId()));
        assertThat(targetItem.getName(), equalTo(itemToSave.getName()));
        assertThat(targetItem.getDescription(), equalTo(itemToUpdate.getDescription()));
        assertThat(targetItem.getAvailable(), equalTo(itemToSave.getAvailable()));
        assertThat(targetItem.getOwner(), equalTo(itemToSave.getOwner()));
    }

    @Test
    void getItemById() {
        User userToSave = User.builder()
                .name("Aleksandr")
                .email("test@yandex.ru")
                .build();
        entityManager.persist(userToSave);

        Item itemToSave = Item.builder()
                .name("сундук")
                .description("классный сундук")
                .available(true)
                .owner(userToSave.getId())
                .comment(new ArrayList<>())
                .build();
        entityManager.persist(itemToSave);

        ItemDto itemDto = itemService.getItemById(itemToSave.getId(), userToSave.getId());

        assertThat(itemDto.getId(), equalTo(itemToSave.getId()));
        assertThat(itemDto.getName(), equalTo(itemToSave.getName()));
        assertThat(itemDto.getDescription(), equalTo(itemToSave.getDescription()));
        assertThat(itemDto.getAvailable(), equalTo(itemToSave.getAvailable()));
        assertThat(itemDto.getOwner(), equalTo(itemToSave.getOwner()));
        assertThat(itemDto.getLastBooking(), nullValue());
        assertThat(itemDto.getNextBooking(), nullValue());
        assertThat(itemDto.getComments().size(), equalTo(0));

    }

    @Test
    void getUserItem() {
        User userToSave = User.builder()
                .name("Aleksandr")
                .email("test@yandex.ru")
                .build();
        entityManager.persist(userToSave);

        Item itemToSave1 = Item.builder()
                .name("сундук")
                .description("классный сундук")
                .available(true)
                .owner(userToSave.getId())
                .comment(new ArrayList<>())
                .build();
        entityManager.persist(itemToSave1);

        Item itemToSave2 = Item.builder()
                .name("ручка")
                .description("красная ручка")
                .available(true)
                .owner(userToSave.getId())
                .comment(new ArrayList<>())
                .build();
        entityManager.persist(itemToSave2);

        User userToSave2 = User.builder()
                .name("Oleg")
                .email("oleg@yandex.ru")
                .build();
        entityManager.persist(userToSave2);

        Booking booking1 = Booking.builder()
                .start(LocalDateTime.of(2022, 10, 10, 2, 2))
                .end(LocalDateTime.of(2022, 11, 11, 3, 3))
                .itemId(itemToSave1.getId())
                .bookerId(userToSave2.getId())
                .status(Status.APPROVED)
                .build();
        entityManager.persist(booking1);

        Booking booking2 = Booking.builder()
                .start(LocalDateTime.of(2024, 10, 10, 2, 2))
                .end(LocalDateTime.of(2024, 11, 11, 3, 3))
                .itemId(itemToSave1.getId())
                .bookerId(userToSave2.getId())
                .status(Status.APPROVED)
                .build();
        entityManager.persist(booking2);


        List<ItemDto> itemsDto = itemService.getUserItem(userToSave.getId(), 0, 32);

        assertThat(itemsDto.size(), equalTo(2));

    }

    @Test
    void searchItem() {
        User userToSave = User.builder()
                .name("Aleksandr")
                .email("test@yandex.ru")
                .build();
        entityManager.persist(userToSave);

        Item itemToSave1 = Item.builder()
                .name("сундук")
                .description("классный сундук")
                .available(true)
                .owner(userToSave.getId())
                .build();
        entityManager.persist(itemToSave1);

        Item itemToSave2 = Item.builder()
                .name("ручка")
                .description("красная ручка")
                .available(true)
                .owner(userToSave.getId())
                .build();
        entityManager.persist(itemToSave2);

        List<Item> targetItems = itemService.searchItem("ручка", 0, 32);

        assertThat(targetItems.get(0).getId(), equalTo(itemToSave2.getId()));
        assertThat(targetItems.get(0).getName(), equalTo(itemToSave2.getName()));
        assertThat(targetItems.get(0).getDescription(), equalTo(itemToSave2.getDescription()));
        assertThat(targetItems.get(0).getAvailable(), equalTo(itemToSave2.getAvailable()));
        assertThat(targetItems.get(0).getOwner(), equalTo(itemToSave2.getOwner()));

    }

    @Test
    void getItemByIdRepository() {
        User userToSave = User.builder()
                .name("Aleksandr")
                .email("test@yandex.ru")
                .build();
        entityManager.persist(userToSave);

        Item itemToSave1 = Item.builder()
                .name("сундук")
                .description("классный сундук")
                .available(true)
                .owner(userToSave.getId())
                .build();
        entityManager.persist(itemToSave1);

        Item targetItem = itemService.getItemByIdRepository(itemToSave1.getId());
        assertThat(targetItem, equalTo(itemToSave1));
    }

    @Test
    void addComment() {
        User userToSave = User.builder()
                .name("Aleksandr")
                .email("test@yandex.ru")
                .build();
        entityManager.persist(userToSave);

        User userToSave2 = User.builder()
                .name("Oleg")
                .email("oleg@yandex.ru")
                .build();
        entityManager.persist(userToSave2);

        Item itemToSave1 = Item.builder()
                .name("сундук")
                .description("классный сундук")
                .available(true)
                .owner(userToSave.getId())
                .build();
        entityManager.persist(itemToSave1);

        Comment comment = Comment.builder().text("Новый комментарий")
                .itemId(itemToSave1.getId())
                .authorId(userToSave.getId())
                .created(LocalDateTime.now()).build();

        Booking booking1 = Booking.builder()
                .start(LocalDateTime.of(2022, 5, 10, 2, 2))
                .end(LocalDateTime.of(2022, 5, 11, 3, 3))
                .itemId(itemToSave1.getId())
                .bookerId(userToSave2.getId())
                .status(Status.APPROVED)
                .build();
        entityManager.persist(booking1);


        CommentDto targetComment = itemService.addComment(comment,userToSave2.getId(),itemToSave1.getId());

        assertThat(targetComment.getText(),equalTo(comment.getText()));


    }


}
