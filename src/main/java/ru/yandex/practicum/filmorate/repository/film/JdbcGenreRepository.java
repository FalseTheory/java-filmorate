package ru.yandex.practicum.filmorate.repository.film;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Repository
public class JdbcGenreRepository implements GenreRepository {


    @Override
    public List<Genre> getByIds(List<Long> ids) {
        return List.of();
    }
}
