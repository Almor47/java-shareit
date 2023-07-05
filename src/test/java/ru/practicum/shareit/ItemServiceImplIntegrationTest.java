package ru.practicum.shareit;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.persistence.EntityManager;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceImplIntegrationTest {

    private final ItemServiceImpl itemService;

    private final EntityManager entityManager;

    private final UserRepository userRepository;



    @Test
    void addItem() {

        User userToSave = User.builder()
                .name("Aleksandr")
                .email("test@yandex.ru")
                .build();
        User saveUser = userRepository.save(userToSave);

        Item itemToSave = Item.builder()
                .name("сундук")
                .description("классный сундук")
                .available(true)
                .owner(saveUser.getId())
                .build();
        itemService.addItem(itemToSave, saveUser.getId());

        val query = entityManager.createQuery(
                "select i from Item i where i.name = :name", Item.class);
        Item item = query.setParameter("name",itemToSave.getName()).getSingleResult();

        assertThat(item.getId(), notNullValue());
        assertThat(item.getName(),equalTo(itemToSave.getName()));
        assertThat(item.getDescription(),equalTo(itemToSave.getDescription()));
        assertThat(item.getAvailable(),equalTo(itemToSave.getAvailable()));
        assertThat(item.getOwner(),equalTo(itemToSave.getOwner()));

    }
}
