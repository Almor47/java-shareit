package ru.practicum.shareit.user;

import lombok.*;


import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.Objects;


@Entity
@Table(name = "users", schema = "public")
@Getter @Setter @ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    @Email
    private String email;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id;
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
