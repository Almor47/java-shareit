package ru.practicum.shareit.item.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

@Getter
@Setter
@ToString
public class ItemDto {

    private long id;

    private String name;

    private String description;

    private Boolean available;

    private long owner;

    private Booking lastBooking;

    private Booking nextBooking;

    private List<CommentDto> comments;

}
