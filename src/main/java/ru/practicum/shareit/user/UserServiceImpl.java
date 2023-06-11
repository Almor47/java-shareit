package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserStorage userStorage;

    @Override
    public User addUser(User user) {
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new EmailUserException("У пользователя пустая почта");
        }
        checkDuplicateEmail(user);
        return userStorage.addUser(user);
    }

    @Override
    public User updateUser(long userId, User user) {
        if (user.getEmail() != null) {
            if (!getUserById(userId).getEmail().equals(user.getEmail())) {
                checkDuplicateEmail(user);
            }
        }
        return userStorage.updateUser(userId, user);
    }

    @Override
    public User getUserById(long userId) {
        return userStorage.getUserById(userId);
    }

    @Override
    public List<User> getAllUser() {
        return userStorage.getAllUser();
    }

    @Override
    public User deleteUserById(long userId) {
        return userStorage.deleteUserById(userId);
    }

    private void checkDuplicateEmail(User user) {
        for (User oneUser : userStorage.getAllUser()) {
            if (oneUser.getEmail().equals(user.getEmail())) {
                throw new SameEmailException("Пользователь на данную почту зарегестрирован");
            }
        }
    }
}
