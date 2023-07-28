package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Item {

    private long id;

    @NotEmpty(message = "У предмета не может быть пустое имя")
    private String name;

    @NotEmpty(message = "У предмета не может быть пустое описание")
    private String description;

    @NotNull(message = "У предмета должна быть доступность")
    private Boolean available;

    private long owner;

    private List<Comment> comment;

    private Long requestId;
}
