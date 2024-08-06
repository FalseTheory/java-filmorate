package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;

public interface FilmService {

    Collection<Film> getAll();

    Film get(long filmId);

    Film save(Film film);

    Film update(Film newFilm);

    List<Film> getPopularFilms(int count);

    void putUserLike(long filmId, long userId);

    void deleteUserLike(long filmId, long userId);
}
