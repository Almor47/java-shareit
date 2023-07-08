package ru.practicum.shareit;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.user.model.User;

import javax.persistence.EntityManager;
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


}
