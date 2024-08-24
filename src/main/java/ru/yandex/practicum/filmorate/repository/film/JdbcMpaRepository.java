package ru.yandex.practicum.filmorate.repository.film;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.MPA;

import java.util.Optional;

@Repository
public class JdbcMpaRepository implements MpaRepository {

    @Override
    public Optional<MPA> getById(long id) {
        return Optional.empty();
    }
}
