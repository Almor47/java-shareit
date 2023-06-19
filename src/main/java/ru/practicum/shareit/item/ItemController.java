package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.model.Item;

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
    public Item getItemById(@PathVariable Long itemId) {
        return itemService.getItemById(itemId);
    }

    @GetMapping
    public List<Item> getUserItem(@RequestHeader(HEADER) Long userId) {
        return itemService.getUserItem(userId);
    }

    @GetMapping("/search")
    public List<Item> searchItem(@RequestParam String text) {
        return itemService.searchItem(text);
    }
}
