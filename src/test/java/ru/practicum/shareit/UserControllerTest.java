package ru.practicum.shareit;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.controller.UserController;
import ru.practicum.shareit.user.exception.EmailUserException;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @SneakyThrows
    @Test
    void saveNewUserSuccessful()  {
        User userToSave = new User();
        userToSave.setEmail("test@yandex.ru");
        when(userService.addUser(any(User.class)))
                .thenReturn(userToSave);

        String response = mvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userToSave)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(userToSave),response);
    }

    @SneakyThrows
    @Test
    void saveNewUserNotValid() {
        User userToSave = new User();
        userToSave.setEmail("email");

        mvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userToSave)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).addUser(userToSave);

    }

    @SneakyThrows
    @Test
    void getAllUser() {
        mvc.perform(get("/users"))
                // строка с контентом необязательна
                .andExpect(status().isOk());

        verify(userService, times(1)).getAllUser();
    }

    @SneakyThrows
    @Test
    void getUserById() {
        long userId = 0L;

        mvc.perform(get("/users/{userId}", userId))
                .andExpect(status().isOk());

        verify(userService, times(1)).getUserById(userId);
    }

    @SneakyThrows
    @Test
    void deleteUserById()  {
        long userId = 0L;

        mvc.perform(delete("/users/{userId}", userId))
                .andExpect(status().isOk());

        verify(userService, times(1)).deleteUserById(userId);
    }

    @SneakyThrows
    @Test
    void updateUser() {
        long userId = 0L;
        User userToUpdate = new User();
        userToUpdate.setEmail("test@yandex.ru");

        mvc.perform(patch("/users/{userId}", userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userToUpdate)))
                .andExpect(status().isOk());

        verify(userService, times(1)).updateUser(userId,userToUpdate);
    }

    @SneakyThrows
    @Test
    void saveNewUserWithException() {

        when(userService.addUser(any(User.class)))
                .thenThrow(new EmailUserException("У пользователя пустая почта"));

        mvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(new User())))
                .andExpect(status().isBadRequest());

    }

    @SneakyThrows
    @Test
    void getUserByIdWithException() {
        long userId = 0L;

        when(userService.getUserById(anyLong()))
                .thenThrow(new UserNotFoundException("Пользователь не найден"));

        mvc.perform(get("/users/{userId}", userId))
                .andExpect(status().isNotFound());

    }
}
