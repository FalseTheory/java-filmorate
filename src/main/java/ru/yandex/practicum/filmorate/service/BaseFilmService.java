package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.film.FilmRepository;
import ru.yandex.practicum.filmorate.repository.user.UserRepository;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BaseFilmService implements FilmService {


    private final FilmRepository filmRepository;
    private final UserRepository userRepository;

    @Override
    public Collection<Film> getAll() {
        return filmRepository.getAll();
    }

    @Override
    public Film get(long filmId) {
        return filmRepository.get(filmId)
                .orElseThrow(() -> new NotFoundException("Фильм с id = " + filmId + " не найден"));
    }

    @Override
    public Film save(Film film) {
        return filmRepository.save(film);
    }

    @Override
    public Film update(Film newFilm) {

        Film oldFilm = filmRepository.get(newFilm.getId())
                .orElseThrow(() -> new NotFoundException("Фильм с id = " + newFilm.getId() + " не найден"));

        oldFilm.setName(newFilm.getName());
        oldFilm.setDescription(newFilm.getDescription());
        oldFilm.setReleaseDate(newFilm.getReleaseDate());
        oldFilm.setDuration(newFilm.getDuration());


        return oldFilm;
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        return filmRepository.getMostPopularFilms(count);
    }

    @Override
    public void putUserLike(long filmId, long userId) {
        User user = userRepository.get(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + userId + " не найден"));
        Film film = filmRepository.get(filmId)
                .orElseThrow(() -> new NotFoundException("Фильм с id = " + filmId + " не найден"));
        filmRepository.addUserLike(film.getId(), user.getId());
    }

    @Override
    public void deleteUserLike(long filmId, long userId) {

        Film film = filmRepository.get(filmId)
                .orElseThrow(() -> new NotFoundException("Фильм с id = " + filmId + " не найден"));

        User user = userRepository.get(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + userId + " не найден"));

        filmRepository.deleteUserLike(film.getId(), user.getId());
    }
}
