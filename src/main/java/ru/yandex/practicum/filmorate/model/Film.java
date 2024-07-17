package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;
import ru.yandex.practicum.filmorate.validators.OnUpdate;

import java.time.LocalDate;

/**
 * Film.
 */
@Data
public class Film {
    @NotNull(groups = OnUpdate.class, message = "id должен быть указан")
    Long id;
    @NotBlank
    @NotNull
    String name;
    @Size(max = 200, message = "Описание не должно превышать 200 символов")
    String description;
    @NotNull
    LocalDate releaseDate;
    @DecimalMin(value = "1", message = "Длительность фильма должна быть представлена положительным числом")
    int duration;


    @AssertFalse(message = "Дата выпуска фильма должна быть не раньше 1895-12-28")
    public boolean isValidReleaseDate() {
        return releaseDate.isBefore(LocalDate.of(1895, 12, 28));
    }

}
