package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    Item addItem(Item item, Long userId);

    Item updateItem(Item item, Long userId, Long itemId);

    ItemDto getItemById(Long itemId, Long userId);

    List<ItemDto> getUserItem(Long userId);

    List<Item> searchItem(String text);

    Item getItemByIdRepository(Long itemId);

    CommentDto addComment(Comment comment, Long userId, Long itemId);

}
