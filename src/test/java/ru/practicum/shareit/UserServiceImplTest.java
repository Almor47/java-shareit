package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.exception.EmailUserException;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void getUserById_whenUserFound_thenReturnUser() {
        long userId = 0L;
        User expectedUser = new User();
        when(userRepository.findById(userId)).thenReturn(Optional.of(expectedUser));

        User actualUser = userService.getUserById(userId);

        assertEquals(expectedUser, actualUser);

    }

    @Test
    void getUserById_whenUserNotFound_thenReturnNotFoundException() {
        long userId = 0L;
        when(userRepository.findById(userId)).thenThrow(
                new UserNotFoundException("Пользователь с id: " + userId + " не найден"));

        assertThrows(UserNotFoundException.class,
                () -> userService.getUserById(0L));

    }

    @Test
    void addUser_whenEmailEmpty_thenReturnEmailUserException() {
        User user = new User();
        user.setEmail("");

        assertThrows(EmailUserException.class, () ->
                userService.addUser(user));

        verify(userRepository, never()).save(user);

    }

    @Test
    void addUser_whenEmailValid_thenSaveUser() {
        User user = new User();
        user.setEmail("test@yandex.ru");

        when(userRepository.save(user)).thenReturn(user);

        User actualUser = userService.addUser(user);

        assertEquals(user, actualUser);
    }
}
