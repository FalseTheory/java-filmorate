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
                .orElseThrow(() -> new NotFoundException("Film with id = " + filmId + " not found"));
    }

    @Override
    public Film save(Film film) {
        return filmRepository.save(film);
    }

    @Override
    public Film update(Film film) {

        filmRepository.update(film);

        return filmRepository.get(film.getId())
                .orElseThrow(() -> new NotFoundException("Film with id = " + film.getId() + " not found"));
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        return filmRepository.getMostPopularFilms(count);
    }

    @Override
    public void putUserLike(long filmId, long userId) {
        User user = userRepository.get(userId)
                .orElseThrow(() -> new NotFoundException("User with id = " + userId + " not found"));
        Film film = filmRepository.get(filmId)
                .orElseThrow(() -> new NotFoundException("Film with id = " + filmId + " not found"));
        filmRepository.addUserLike(film.getId(), user.getId());
    }

    @Override
    public void deleteUserLike(long filmId, long userId) {

        Film film = filmRepository.get(filmId)
                .orElseThrow(() -> new NotFoundException("Film with id = " + filmId + " not found"));

        User user = userRepository.get(userId)
                .orElseThrow(() -> new NotFoundException("User with id = " + userId + " not found"));

        filmRepository.deleteUserLike(film.getId(), user.getId());
    }
}
