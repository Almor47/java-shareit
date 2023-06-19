package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage {
    Item addItem(Item item, Long userId);

    Item updateItem(Item item, Long userId, Long itemId);

    Item getItemById(Long itemId);

    List<Item> getUserItem(Long userId);

    List<Item> searchItem(String text);
}
