package ru.practicum.shareit;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.request.service.RequestServiceImpl;
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
public class ItemRequestServiceImplIntegrationTest {

    private final RequestServiceImpl requestService;

    private final EntityManager entityManager;

    private final RequestRepository requestRepository;

    @Test
    void saveRequest() {
        User userToSave = User.builder()
                .name("Aleksandr")
                .email("test@yandex.ru")
                .build();
        entityManager.persist(userToSave);

        ItemRequest itemRequestToSave = ItemRequest.builder()
                .description("Супер тихая дрель")
                .created(LocalDateTime.now())
                .build();
        requestService.addRequest(itemRequestToSave, userToSave.getId());

        val query = entityManager.createQuery(
                "select ir from ItemRequest ir where ir.description = :description", ItemRequest.class);
        ItemRequest itemRequest = query.setParameter("description", itemRequestToSave.getDescription())
                .getSingleResult();

        assertThat(itemRequest.getId(), notNullValue());
        assertThat(itemRequest.getDescription(), equalTo(itemRequestToSave.getDescription()));
        assertThat(itemRequest.getRequestor(), equalTo(userToSave.getId()));

    }

    @Test
    void getOwnRequest() {
        User userToSave = User.builder()
                .name("Aleksandr")
                .email("test@yandex.ru")
                .build();
        entityManager.persist(userToSave);

        ItemRequest itemRequestToSave1 = ItemRequest.builder()
                .description("Старый телевизор ЧБ")
                .requestor(userToSave.getId())
                .created(LocalDateTime.now())
                .build();
        entityManager.persist(itemRequestToSave1);

        ItemRequest itemRequestToSave2 = ItemRequest.builder()
                .description("Вентилятор")
                .requestor(userToSave.getId())
                .created(LocalDateTime.now())
                .build();
        entityManager.persist(itemRequestToSave2);

        List<ItemRequestDto> targetRequests =
                requestService.getOwnRequest(userToSave.getId());

        assertThat(targetRequests.size(), equalTo(2));


    }

    @Test
    void getOtherRequest() {
        User userToSave1 = User.builder()
                .name("Aleksandr")
                .email("test@yandex.ru")
                .build();
        entityManager.persist(userToSave1);

        User userToSave2 = User.builder()
                .name("Oleg")
                .email("oleg@yandex.ru")
                .build();
        entityManager.persist(userToSave2);


        ItemRequest itemRequestToSave1 = ItemRequest.builder()
                .description("Старый телевизор ЧБ")
                .requestor(userToSave1.getId())
                .created(LocalDateTime.now())
                .build();
        entityManager.persist(itemRequestToSave1);

        ItemRequest itemRequestToSave2 = ItemRequest.builder()
                .description("Вентилятор быстрый")
                .requestor(userToSave1.getId())
                .created(LocalDateTime.now())
                .build();
        entityManager.persist(itemRequestToSave2);

        ItemRequest itemRequestToSave3 = ItemRequest.builder()
                .description("Музыкальный центр")
                .requestor(userToSave1.getId())
                .created(LocalDateTime.now())
                .build();
        entityManager.persist(itemRequestToSave3);

        List<ItemRequestDto> targetRequests =
                requestService.getOtherRequest(userToSave2.getId(), 0, 32);
        System.out.println(targetRequests);

        assertThat(targetRequests.size(), equalTo(3));
    }

    @Test
    void getRequest() {
        User userToSave1 = User.builder()
                .name("Aleksandr")
                .email("test@yandex.ru")
                .build();
        entityManager.persist(userToSave1);

        ItemRequest itemRequestToSave1 = ItemRequest.builder()
                .description("Старый телевизор ЧБ")
                .requestor(userToSave1.getId())
                .created(LocalDateTime.now())
                .build();
        entityManager.persist(itemRequestToSave1);

        ItemRequestDto targetRequest =
                requestService.getRequest(userToSave1.getId(), itemRequestToSave1.getId());

        assertThat(targetRequest.getId(), notNullValue());
        assertThat(targetRequest.getDescription(), equalTo(itemRequestToSave1.getDescription()));

    }

    @AfterEach
    void cleanData() {
        requestRepository.deleteAll();
    }

}
