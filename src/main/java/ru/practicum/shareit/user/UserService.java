package ru.practicum.shareit.user;

import java.util.List;

public interface UserService {

    User addUser(User user);

    User updateUser(long userId, User user);

    User deleteUserById(long userId);

    User getUserById(long userId);

    List<User> getAllUser();
}
