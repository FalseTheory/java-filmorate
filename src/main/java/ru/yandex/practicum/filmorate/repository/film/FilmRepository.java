package ru.yandex.practicum.filmorate.repository.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface FilmRepository {

    Film save(Film film);

    void update(Film film);

    Collection<Film> getAll();

    Optional<Film> get(long filmId);

    void addUserLike(long filmId, long userId);

    void deleteUserLike(long filmId, long userId);

    List<Film> getMostPopularFilms(int count);

}
