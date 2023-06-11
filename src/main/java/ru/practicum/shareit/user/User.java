package ru.practicum.shareit.user;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Builder
public class User {

    private long id;
    private String name;
    @Email
    private String email;

}
