package ru.practicum.shareit.request;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ItemRequest {

    private long id;
    private String description;
    private long requestor;
    private LocalDateTime created;
}
