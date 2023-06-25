package ru.practicum.shareit.booking.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class BookingDto {

    private Long itemId;

    private LocalDateTime start;

    private LocalDateTime end;

}
