package ru.practicum.shareit.item;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ItemStorageImpl implements ItemStorage {

    private final HashMap<Long, Item> itemMap = new HashMap<>();
    long countItem = 1;


    @Override
    public Item addItem(Item item, Long userId) {
        item.setOwner(userId);
        item.setId(countItem);
        itemMap.put(countItem, item);
        countItem++;
        return item;
    }

    @Override
    public Item updateItem(Item item, Long userId, Long itemId) {
        Item itemFromMap = itemMap.get(itemId);
        if (item.getName() != null) {
            itemFromMap.setName(item.getName());
        }
        if (item.getDescription() != null) {
            itemFromMap.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            itemFromMap.setAvailable(item.getAvailable());
        }
        itemMap.put(itemId, itemFromMap);
        return itemFromMap;
    }

    @Override
    public Item getItemById(Long itemId) {
        return itemMap.get(itemId);
    }

    @Override
    public List<Item> getUserItem(Long userId) {
        return itemMap.values()
                .stream()
                .filter(x -> x.getOwner() == userId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> searchItem(String text) {
        String textMod = text.toLowerCase();
        return itemMap.values()
                .stream()
                .filter(x -> filterForSearch(x,textMod))
                .collect(Collectors.toList());
    }

    private boolean filterForSearch(Item item,String textMod) {
        if(!item.getAvailable()) {
            return false;
        }
        return item.getDescription().toLowerCase().contains(textMod) ||
                item.getName().toLowerCase().contains(textMod);
    }


}
