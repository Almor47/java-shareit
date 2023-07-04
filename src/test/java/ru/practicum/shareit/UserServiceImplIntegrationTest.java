package ru.practicum.shareit;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserServiceImpl;

import javax.persistence.EntityManager;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;


@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceImplIntegrationTest {

    private final UserServiceImpl userService;

    private final EntityManager entityManager;

    @Test
    void saveUser() {
        User userToSave = User.builder().name("Aleksandr").email("test@yandex.ru").build();
        userService.addUser(userToSave);

        val query = entityManager.createQuery(
                "select u from User u where u.email = :email", User.class);
        User user = query.setParameter("email",userToSave.getEmail()).getSingleResult();

        assertThat(user.getId(), notNullValue());
        assertThat(user.getName(),equalTo(userToSave.getName()));
        assertThat(user.getEmail(),equalTo(user.getEmail()));

    }
}
