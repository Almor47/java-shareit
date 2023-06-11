package ru.practicum.shareit.user;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class UserStorageImpl implements UserStorage {

    HashMap<Long, User> userMap = new HashMap<>();
    long countUser = 1;

    @Override
    public User addUser(User user) {
        user.setId(countUser);
        userMap.put(countUser, user);
        countUser++;
        return user;
    }

    @Override
    public User updateUser(long userId, User user) {
        User userFromMap = userMap.get(userId);
        if (user.getEmail() != null) {
            userFromMap.setEmail(user.getEmail());
        }
        if (user.getName() != null) {
            userFromMap.setName(user.getName());
        }
        userMap.put(userId, userFromMap);
        return userFromMap;
    }

    @Override
    public User getUserById(long userId) {
        return userMap.get(userId);
    }

    @Override
    public User deleteUserById(long userId) {
        User user = userMap.get(userId);
        userMap.remove(userId);
        return user;
    }

    @Override
    public List<User> getAllUser() {
        return new ArrayList<>(userMap.values());
    }

}