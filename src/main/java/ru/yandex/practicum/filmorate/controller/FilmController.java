package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validators.OnUpdate;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
public class FilmController {

    private static final Logger log = LoggerFactory.getLogger(FilmController.class);

    private Long idCount = 0L;

    Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> findAll() {
        return films.values();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.trace("calculating new id");
        film.setId(++idCount);
        films.put(film.getId(), film);
        log.info("successfully created film record - {}", film);
        return film;
    }

    @PutMapping
    public Film update(@Validated(OnUpdate.class) @RequestBody Film newFilm) {
        log.info("trying to update film record");
        log.debug("updating film with id-{}", newFilm.getId());
        Film oldFilm = films.get(newFilm.getId());
        if (oldFilm == null) {
            throw new NotFoundException("Фильм с id = " + newFilm.getId() + " не найден");
        }
        log.debug("old film record - {}", oldFilm);
        log.debug("new film record - {}", newFilm);

        oldFilm.setName(newFilm.getName());
        oldFilm.setDescription(newFilm.getDescription());
        oldFilm.setReleaseDate(newFilm.getReleaseDate());
        oldFilm.setDuration(newFilm.getDuration());

        log.debug("resulted film record - {}", oldFilm);
        log.info("successfully updated film record with id - {}", oldFilm.getId());

        return oldFilm;


    }


}
