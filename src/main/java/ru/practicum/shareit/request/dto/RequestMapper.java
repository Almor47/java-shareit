package ru.practicum.shareit.request.dto;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RequestMapper {

    public static List<ItemRequestDto> ListItemRequestToItemRequestDto(List<ItemRequest> itemRequests, List<Item> items) {
        List<ItemRequestDto> ans = new ArrayList<>();
        for (ItemRequest one : itemRequests) {
            ItemRequestDto itemRequestDto = new ItemRequestDto();
            itemRequestDto.setId(one.getId());
            itemRequestDto.setDescription(one.getDescription());
            itemRequestDto.setCreated(one.getCreated());
            List<Item> itemsForRequest = new ArrayList<>();
            for (Item item : items) {
                if (Objects.equals(item.getRequestId(), one.getId())) {
                    itemsForRequest.add(item);
                }
            }
            itemRequestDto.setItems(itemsForRequest);
            ans.add(itemRequestDto);
        }
        return ans;
    }

    public static ItemRequestDto itemRequestToItemRequestDto(ItemRequest itemRequests, List<Item> items) {

        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setId(itemRequests.getId());
        itemRequestDto.setDescription(itemRequests.getDescription());
        itemRequestDto.setCreated(itemRequests.getCreated());
        List<Item> itemsForRequest = new ArrayList<>();
        for (Item item : items) {
            if (Objects.equals(item.getRequestId(), itemRequests.getId())) {
                itemsForRequest.add(item);
            }
        }
        itemRequestDto.setItems(itemsForRequest);
        return itemRequestDto;
    }
}
