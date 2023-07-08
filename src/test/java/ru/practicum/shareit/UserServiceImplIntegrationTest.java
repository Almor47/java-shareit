package ru.practicum.shareit;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserServiceImpl;

import javax.persistence.EntityManager;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

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
        User user = query.setParameter("email", userToSave.getEmail()).getSingleResult();

        assertThat(user.getId(), notNullValue());
        assertThat(user.getName(), equalTo(userToSave.getName()));
        assertThat(user.getEmail(), equalTo(user.getEmail()));

    }

    @Test
    void getAllUsers() {
        User user1 = User.builder().name("Aleks").email("Aleks@yandex.ru").build();
        User user2 = User.builder().name("Ivan").email("Ivan@yandex.ru").build();
        User user3 = User.builder().name("Oleg").email("Oleg@yandex.ru").build();
        List<User> sourceUsers = List.of(
                user1,
                user2,
                user3
        );

        for (User user : sourceUsers) {
            entityManager.persist(user);
        }
        entityManager.flush();

        List<User> targetUsers = userService.getAllUser();

        assertThat(targetUsers, hasSize(sourceUsers.size()));
        for (User sourceUser : sourceUsers) {
            assertThat(targetUsers, hasItem(allOf(
                    hasProperty("id", notNullValue()),
                    hasProperty("name", equalTo(sourceUser.getName())),
                    hasProperty("email", equalTo(sourceUser.getEmail()))
            )));
        }
    }

    @Test
    void getUserById() {
        User user1 = User.builder().name("Aleks").email("Aleks@yandex.ru").build();
        entityManager.persist(user1);
        entityManager.flush();

        User targetUser = userService.getUserById(user1.getId());

        assertThat(targetUser.getId(), notNullValue());
        assertThat(targetUser.getName(), equalTo(user1.getName()));
        assertThat(targetUser.getEmail(), equalTo(user1.getEmail()));

    }

    @Test
    void deleteUserById() {
        User user1 = User.builder().name("Aleks").email("Aleks@yandex.ru").build();
        entityManager.persist(user1);
        entityManager.flush();

        userService.deleteUserById(user1.getId());
        List<User> targetUsers = userService.getAllUser();

        assertThat(0, equalTo(targetUsers.size()));
    }

    @Test
    void updateUser() {
        User user1 = User.builder().name("Aleks").email("Aleks@yandex.ru").build();
        entityManager.persist(user1);
        entityManager.flush();

        long userId = user1.getId();
        User user2 = User.builder().name("NewName").email("NewEmail").build();

        User targetUser = userService.updateUser(userId, user2);

        assertThat(targetUser.getId(), notNullValue());
        assertThat(targetUser.getName(), equalTo(user2.getName()));
        assertThat(targetUser.getEmail(), equalTo(user2.getEmail()));


    }
}
