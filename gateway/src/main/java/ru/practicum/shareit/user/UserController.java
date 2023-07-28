package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.User;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated

public class UserController {

    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> addUser(@RequestBody @Valid User user) {
        log.info("Add new user");
        return userClient.addUser(user);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@PathVariable Long userId, @RequestBody @Valid User user) {
        log.info("Update user with userId={}", userId);
        return userClient.updateUser(userId, user);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUserById(@PathVariable Long userId) {
        log.info("Delete user with userId={}", userId);
        return userClient.deleteUserById(userId);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUserById(@PathVariable Long userId) {
        log.info("Get user with userId={}", userId);
        return userClient.getUserById(userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllUser() {
        log.info("Get all user");
        return userClient.getAllUser();
    }

}




