package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.film.GenreRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BaseGenreService implements GenreService {

    private final GenreRepository genreRepository;

    @Override
    public List<Genre> getAll() {
        return genreRepository.getAll();
    }

    @Override
    public Genre get(long id) {
        return genreRepository.getById(id)
                .orElseThrow(() -> new NotFoundException("Genre With id - " + id + " not found"));
    }
}
