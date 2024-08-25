package ru.yandex.practicum.filmorate.repository.film;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreRepository {

    List<Genre> getByIds(List<Long> ids);
    Optional<Genre> getById(long id);
    List<Genre> getByFilmId(long id);
    List<Genre> getAll();
}
