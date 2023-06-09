package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;
    private static final String HEADER = "X-Sharer-User-Id";

    @PostMapping
    public Item addItem(@RequestBody Item item, @RequestHeader(HEADER) Long userId) {
        return itemService.addItem(item, userId);
    }

    @PatchMapping("/{itemId}")
    public Item updateItem(@RequestBody Item item, @RequestHeader(HEADER) Long userId,
                           @PathVariable Long itemId) {
        return itemService.updateItem(item, userId, itemId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable Long itemId, @RequestHeader(HEADER) Long userId) {
        return itemService.getItemById(itemId, userId);
    }

    @GetMapping
    public List<ItemDto> getUserItem(@RequestHeader(HEADER) Long userId,
                                     @RequestParam(defaultValue = "0") Integer from,
                                     @RequestParam(defaultValue = "32") Integer size) {
        return itemService.getUserItem(userId,from,size);
    }

    @GetMapping("/search")
    public List<Item> searchItem(@RequestParam String text,
                                 @RequestParam(defaultValue = "0") Integer from,
                                 @RequestParam(defaultValue = "32") Integer size) {
        return itemService.searchItem(text,from,size);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@RequestBody Comment comment,
                                 @RequestHeader(HEADER) Long userId,
                                 @PathVariable Long itemId) {
        return itemService.addComment(comment, userId, itemId);
    }


}
