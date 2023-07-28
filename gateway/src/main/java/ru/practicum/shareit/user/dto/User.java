package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private long id;

    private String name;

    @Email(message = "Некорректная электронная почта")
    private String email;

}