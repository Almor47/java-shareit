package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserNotFoundException;
import ru.practicum.shareit.user.UserStorage;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ItemServiceImpl implements ItemService {

    private final ItemStorage itemStorage;
    private final UserStorage userStorage;

    @Override
    public Item addItem(Item item, Long userId) {
        if (userId == null) {
            throw new UserNotFoundException("У предмета отсутствует пользователь");
        } else if (userStorage.getUserById(userId) == null) {
            throw new UserNotFoundException("Пользователя с таким айди " + userId + " не существует");
        } else if (item.getAvailable() == null) {
            throw new BadRequestItemException("У предмета отсутствует доступность");
        } else if (item.getName() == null || item.getName().isEmpty()) {
            throw new BadRequestItemException("У предмета отсутствует имя");
        } else if (item.getDescription() == null || item.getDescription().isEmpty()) {
            throw new BadRequestItemException("У предмета отсутствует описание");
        }
        return itemStorage.addItem(item, userId);
    }

    @Override
    public Item updateItem(Item item, Long userId, Long itemId) {
        if (userId == null) {
            throw new UpdateWithoutXSharerException("Запрос без X-Sharer-User-Id");
        }
        User user = userStorage.getUserById(userId);
        if (user == null) {
            throw new UserNotFoundException("Пользователь с айди " + userId + " не найден");
        }
        long owner = itemStorage.getItemById(itemId).getOwner();
        if (owner != userId) {
            throw new UserNotFoundException("Пользователь с айди " + userId + " не является владельцем вещи");
        }
        return itemStorage.updateItem(item, userId, itemId);
    }

    @Override
    public Item getItemById(Long itemId) {
        return itemStorage.getItemById(itemId);
    }

    @Override
    public List<Item> getUserItem(Long userId) {
        return itemStorage.getUserItem(userId);
    }

    @Override
    public List<Item> searchItem(String text) {
        if (text == null || text.isEmpty()) {
            return new ArrayList<>();
        }
        return itemStorage.searchItem(text);
    }
}
