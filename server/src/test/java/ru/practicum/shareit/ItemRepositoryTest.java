package ru.practicum.shareit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class ItemRepositoryTest {
    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void createItemForTest() {

        User user = User.builder()
                .email("user@yandex.ru")
                .name("Aleksandr")
                .build();

        User savedUser = userRepository.save(user);

        Item itemToSave1 = Item.builder()
                .name("легкий сундук")
                .description("классный сундук")
                .available(true)
                .owner(savedUser.getId())
                .build();

        Item itemToSave2 = Item.builder()
                .name("ручка")
                .description("красная, тонкая и легкая")
                .available(true)
                .owner(savedUser.getId())
                .build();

        itemRepository.save(itemToSave1);
        itemRepository.save(itemToSave2);

    }

    @Test
    void searchItem_whenItemFound_thenReturnItem() {
        String text = "легк";
        Pageable page = PageRequest.of(0,32);
        List<Item> expectedItems = itemRepository.search(text, page);

        assertEquals(2,expectedItems.size());
    }

    @Test
    void searchItem_whenItemNotFound_thenReturnEmptyList() {
        String text = "телевизор";
        Pageable page = PageRequest.of(0,32);
        List<Item> expectedItems = itemRepository.search(text, page);

        assertEquals(0,expectedItems.size());
    }

    @AfterEach
    void deleteAllUser() {
        itemRepository.deleteAll();
    }
}
