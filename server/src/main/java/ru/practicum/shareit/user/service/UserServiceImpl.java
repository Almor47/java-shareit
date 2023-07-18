package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.exception.EmailUserException;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Transactional
    @Override
    public User addUser(User user) {
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new EmailUserException("У пользователя пустая почта");
        }
        return userRepository.save(user);
    }

    @Transactional
    @Override
    public User updateUser(long userId, User user) {
        User userDb = getUserById(userId);
        if (user.getEmail() != null) {
            userDb.setEmail(user.getEmail());
        }
        if (user.getName() != null) {
            userDb.setName(user.getName());
        }

        return userRepository.save(userDb);
    }

    @Override
    public User getUserById(long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException("Пользователь с id: " + userId + " не найден"));
    }

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    @Transactional
    @Override
    public User deleteUserById(long userId) {
        User user = getUserById(userId);
        userRepository.deleteById(userId);
        return user;
    }

}
