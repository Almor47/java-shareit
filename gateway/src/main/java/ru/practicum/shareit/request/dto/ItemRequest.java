package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequest {

    private Long id;
    @NotEmpty(message = "Описание запроса не может быть пустым")
    private String description;

    private Long requestor;

    private LocalDateTime created = LocalDateTime.now();

}
