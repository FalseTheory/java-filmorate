package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

/**
 * User.
 */
@Data
public class User {
    Long id;
    @NotBlank
    @Email
    String email;
    @NotNull
    @NotBlank
    String login;
    String name;
    @NotNull
    LocalDate birthday;
}
