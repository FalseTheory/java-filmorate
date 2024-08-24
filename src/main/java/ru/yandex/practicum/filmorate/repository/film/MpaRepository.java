package ru.yandex.practicum.filmorate.repository.film;

import ru.yandex.practicum.filmorate.model.MPA;

import java.util.Optional;

public interface MpaRepository {

    Optional<MPA> getById(long id);
}
