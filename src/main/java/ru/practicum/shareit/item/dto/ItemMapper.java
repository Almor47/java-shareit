package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;

public class ItemMapper {

    public static ItemDto mapToItemDto(Item item, Booking bookingLast, Booking bookingNext) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setOwner(item.getOwner());
        itemDto.setAvailable(item.getAvailable());
        itemDto.setLastBooking(bookingLast);
        itemDto.setNextBooking(bookingNext);
        return itemDto;
    }
}
