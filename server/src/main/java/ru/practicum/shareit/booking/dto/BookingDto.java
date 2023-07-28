package ru.practicum.shareit.booking.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingDto {

    private Long itemId;

    private LocalDateTime start;

    private LocalDateTime end;

}
