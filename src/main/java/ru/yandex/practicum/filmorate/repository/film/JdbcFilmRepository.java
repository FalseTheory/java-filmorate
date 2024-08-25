package ru.yandex.practicum.filmorate.repository.film;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.mappers.FilmExtractor;
import ru.yandex.practicum.filmorate.repository.mappers.FilmRowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class JdbcFilmRepository implements FilmRepository {



    private final NamedParameterJdbcOperations jdbc;
    private final GenreRepository genreRepository;
    private final FilmRowMapper mapper;
    private final FilmExtractor extractor;



    private static final String INSERT_QUERY = "INSERT INTO films(name, description, release_date, duration, rating)" +
                                               "VALUES (:name, :description, :release_date, :duration, :rating) returning id";
    private static final String GENRES_CONNECT_QUERY = "INSERT INTO GENRES (id, name) VALUES (:id, :name)";
    private static final String GET_BY_ID_QUERY = "SELECT * FROM FILMS\n" +
                                                  "JOIN MPA_RATING mr ON mr.\"rating_id\" = \"rating\"\n" +
                                                  "JOIN FILM_GENRES fg ON fg.\"film_id\" = \"id\"\n" +
                                                  "JOIN GENRES g ON g.\"genre_id\" = fg.\"genre\"\n" +
                                                  "WHERE \"id\" = :id;";
    private static final String GET_ALL_QUERY = "SELECT * FROM FILMS\n" +
                                                "JOIN MPA_RATING mr ON mr.\"rating_id\" = \"rating\";";
    private static final String ADD_USER_LIKE_QUERY = "";
    private static final String DELETE_USER_LIKE_QUERY = "";
    private static final String GET_MOST_POPULAR_FILMS_QUERY = "";
    private static final String UPDATE_QUERY = "UPDATE films SET name = ?, description = ?, release_date = ?," +
                                               " duration = ?, rating = ? WHERE id = ?";

    @Override
    public Film save(Film film) {
        //Доделать
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("name", film.getName());
        mapSqlParameterSource.addValue("description", film.getDescription());
        mapSqlParameterSource.addValue("release_date", film.getReleaseDate());
        mapSqlParameterSource.addValue("duration", film.getDuration());
        mapSqlParameterSource.addValue("rating", film.getMpa());

        jdbc.update(INSERT_QUERY, mapSqlParameterSource, keyHolder);
        film.setId(keyHolder.getKeyAs(Long.class));


        for(Genre genre : film.getGenres()) {
            mapSqlParameterSource = new MapSqlParameterSource();
            mapSqlParameterSource.addValue("id", film.getId());
            mapSqlParameterSource.addValue("name", genre.getName());
            jdbc.update(GENRES_CONNECT_QUERY, mapSqlParameterSource);
        }

        return film;

    }

    @Override
    public void update(Film film) {
        // Обновить фильм и его рейтинг UPDATE_QUERY
        // DELETE_QUERY Удалить связи фильм-жанры
        // батч INSERT всех связей жанр-фильм
        // jdbc.batchUpdate

    }

    @Override
    public List<Film> getAll() {
        List<Film> filmList = jdbc.query(GET_ALL_QUERY, mapper);

        for(Film film : filmList) {
            film.setGenres(new LinkedHashSet<>(genreRepository.getByFilmId(film.getId())));
        }
        return filmList;
    }

    @Override
    public Optional<Film> get(long filmId) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("id", filmId);
        try {
            Film res = jdbc.query(GET_BY_ID_QUERY, mapSqlParameterSource, extractor);
            return Optional.ofNullable(res);
        } catch (EmptyResultDataAccessException ignored) {
            return Optional.empty();
        }

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
