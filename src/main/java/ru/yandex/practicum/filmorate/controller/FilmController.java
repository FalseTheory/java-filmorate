package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
public class FilmController {

    private static final Logger log = LoggerFactory.getLogger(FilmController.class);

    Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> findAll() {
        return films.values();
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        log.info("trying to create new Film record");
        try {
            log.debug("parsed film record - {}", film);
            if (film.getName() == null || film.getName().isBlank()) {
                throw new ValidationException("Название фильма не должно быть пустым");
            }
            if (film.getDescription() != null && film.getDescription().length() > 200) {
                throw new ValidationException("Описание фильма не может быть свыше 200 символов");
            }
            if (film.getReleaseDate() == null ||
                    film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
                throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года");
            }
            if (film.getDuration() < 1) {
                throw new ValidationException("Продолжительность фильма должна быть положительным числом");
            }
        } catch (ValidationException e) {
            log.error("error while trying to validate request body: ", e);
            throw new RuntimeException(e);
        }
        log.trace("calculating new id");
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("film record successfully created");
        return film;
    }

    @PutMapping
    public Film update(@RequestBody Film newFilm) {
        log.info("trying to update film record");
        try {
            if (newFilm.getId() == null) {
                throw new ConditionsNotMetException("id должен быть указан");
            }
            log.debug("updating film with id-{}", newFilm.getId());
            if (films.containsKey(newFilm.getId())) {
                Film oldFilm = films.get(newFilm.getId());
                log.debug("old film record - {}", oldFilm);
                log.debug("new film record - {}", newFilm);
                if (newFilm.getName() != null && !newFilm.getName().isBlank()) {
                    oldFilm.setName(newFilm.getName());
                }
                if (newFilm.getDescription() != null && !newFilm.getDescription().isBlank()
                        && !(newFilm.getDescription().length() > 200)) {
                    oldFilm.setDescription(newFilm.getDescription());
                }
                if (newFilm.getReleaseDate() != null) {
                    oldFilm.setReleaseDate(newFilm.getReleaseDate());
                }

                oldFilm.setDuration(newFilm.getDuration());
                log.debug("resulted film record - {}", oldFilm);

                log.info("film record with id - {} updated successfully", oldFilm.getId());

                return oldFilm;
            }
            throw new NotFoundException("Фильм с id = " + newFilm.getId() + " не найден");
        } catch (Exception e) {
            log.error("error while trying to update film record: ", e);
            throw new RuntimeException(e);
        }

    }

    private Long getNextId() {
        long currentLastId = films.keySet().stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        log.trace("{} is next available id", currentLastId + 1);
        return ++currentLastId;
    }


}
