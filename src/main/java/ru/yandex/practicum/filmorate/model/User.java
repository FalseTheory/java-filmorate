package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;
import ru.yandex.practicum.filmorate.validators.OnUpdate;

import java.time.LocalDate;

/**
 * User.
 */
@Data
public class User {
    @NotNull(groups = OnUpdate.class, message = "id должен быть указан")
    Long id;
    @NotBlank
    @Email(message = "email должен быть корректным")
    String email;
    @NotNull
    @NotBlank
    String login;
    String name;
    @NotNull
    @PastOrPresent(message = "День рождения не может быть в будущем")
    LocalDate birthday;

    @AssertFalse(message = "Логин не может содержать пробелы")
    public boolean isLoginValid() {
        return login.contains(" ");
    }
}
