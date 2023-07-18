package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    private Long id;

    @NotEmpty(message = "Комментарий не может быть пустым")
    private String text;

    private Long itemId;

    private Long authorId;

    private LocalDateTime created;
}
