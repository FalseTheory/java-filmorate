package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.film.FilmRepository;
import ru.yandex.practicum.filmorate.repository.film.GenreRepository;
import ru.yandex.practicum.filmorate.repository.film.MpaRepository;
import ru.yandex.practicum.filmorate.repository.user.UserRepository;

import java.util.LinkedHashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BaseFilmService implements FilmService {

    private final FilmRepository filmRepository;
    private final UserRepository userRepository;
    private final MpaRepository mpaRepository;
    private final GenreRepository genreRepository;


    @Override
    public List<Film> getAll() {
        return filmRepository.getAll();
    }

    @Override
    public Film get(long filmId) {
        return filmRepository.get(filmId)
                .orElseThrow(() -> new NotFoundException("Film with id = " + filmId + " not found"));
    }

    @Override
    public Film save(Film film) {

        film.setMpa(mpaRepository.getById(film.getMpa().getId())
                .orElseThrow(() -> new DataIntegrityViolationException("Mpa with id = " + film.getId() + " not found")));
        if (film.getGenres() != null) {
            final List<Long> genreIds = film.getGenres()
                    .stream().map(Genre::getId).toList();

            final List<Genre> genres = genreRepository.getByIds(genreIds);
            if (genreIds.size() != genres.size()) {
                throw new DataIntegrityViolationException("Жанры не найдены");
            }
            film.setGenres(new LinkedHashSet<>(genres));
        }


        return filmRepository.save(film);
    }

    @Override
    public Film update(Film dto) {

        Film film = filmRepository.get(dto.getId())
                .orElseThrow(() -> new NotFoundException("Film with id = " + dto.getId() + " not found"));
        Mpa newMpa = mpaRepository.getById(dto.getMpa().getId())
                .orElseThrow(() -> new NotFoundException("Mpa with id = " + dto.getId() + " not found"));

        if (dto.getGenres() != null) {
            final List<Long> genreIds = dto.getGenres()
                    .stream().map(Genre::getId).toList();

            final List<Genre> genres = genreRepository.getByIds(genreIds);

            if (genreIds.size() != genres.size()) {
                throw new NotFoundException("Жанры не найдены");
            }
            film.setGenres(dto.getGenres());
        }

        film.setName(dto.getName());
        film.setDuration(dto.getDuration());
        film.setDescription(dto.getDescription());
        film.setMpa(newMpa);
        film.setReleaseDate(dto.getReleaseDate());

        filmRepository.update(film);

        return film;
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
