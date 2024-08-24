package ru.yandex.practicum.filmorate.repository.film;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreRepository {

    List<Genre> getByIds(List<Long> ids);
}
