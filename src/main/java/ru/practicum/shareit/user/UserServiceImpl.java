package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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
        checkDuplicateEmail(user);
        return userRepository.save(user);
    }
    @Transactional
    @Override
    public User updateUser(long userId, User user) {
        if (user.getEmail() != null && !getUserById(userId).getEmail().equals(user.getEmail())) {
            checkDuplicateEmail(user);
        }
        return userRepository.save(user);
    }

    @Override
    public User getUserById(long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException("Пользователь с id: " + userId + "не найден"));
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

    private void checkDuplicateEmail(User user) {
        for (User oneUser : getAllUser()) {
            if (oneUser.getEmail().equals(user.getEmail())) {
                throw new SameEmailException("Пользователь на данную почту зарегестрирован");
            }
        }
    }
}
