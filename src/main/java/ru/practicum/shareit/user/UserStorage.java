package ru.practicum.shareit.user;

import java.util.List;

public interface UserStorage {

    User addUser(User user);

    User updateUser(long userId, User user);

    User getUserById(long userId);

    User deleteUserById(long userId);

    List<User> getAllUser();
}
