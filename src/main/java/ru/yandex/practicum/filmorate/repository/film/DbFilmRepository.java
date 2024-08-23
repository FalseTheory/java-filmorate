package ru.yandex.practicum.filmorate.repository.film;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.BaseDbRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository("dbFilmRepository")
public class DbFilmRepository extends BaseDbRepository<Film> implements FilmRepository {

    public DbFilmRepository(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Film save(Film film) {
        return null;
    }

    @Override
    public void update(Film film) {

    }

    @Override
    public Collection<Film> getAll() {
        return List.of();
    }

    @Override
    public Optional<Film> get(long filmId) {
        return Optional.empty();
    }

    @Override
    public void addUserLike(long filmId, long userId) {

    }

    @Override
    public void deleteUserLike(long filmId, long userId) {

    }

    @Override
    public List<Film> getMostPopularFilms(int count) {
        return List.of();
    }
}
