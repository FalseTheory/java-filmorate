package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.validators.OnUpdate;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
@RequiredArgsConstructor
public class FilmController {


    private final FilmService filmService;


    @GetMapping
    public Collection<Film> findAll() {
        log.info("Get all films");
        return filmService.getAll();
    }

    @GetMapping("/{filmId}")
    public Film get(@PathVariable Long filmId) {
        log.info("get film by id = {}", filmId);
        return filmService.get(filmId);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") int count) {
        log.info("get {} most popular films", count);
        return filmService.getPopularFilms(count);
    }

    @PutMapping("/{filmId}/like/{userId}")
    public void putLike(@PathVariable Long filmId, @PathVariable Long userId) {
        log.info("put user - {} like to film - {}", userId, filmId);
        filmService.putUserLike(filmId, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public void deleteLike(@PathVariable Long filmId, @PathVariable Long userId) {
        log.info("delete user - {} like from film - {}", userId, filmId);
        filmService.deleteUserLike(filmId, userId);
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.info("create film = {}", film.toString());
        return filmService.save(film);
    }

    @PutMapping
    public Film update(@Validated(OnUpdate.class) @RequestBody Film newFilm) {
        log.info("updating film by id = {}, with body = {}", newFilm.getId(), newFilm.toString());
        return filmService.update(newFilm);
    }


}
