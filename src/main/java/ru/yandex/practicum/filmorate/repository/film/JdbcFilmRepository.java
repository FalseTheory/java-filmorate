package ru.yandex.practicum.filmorate.repository.film;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.mappers.FilmExtractor;
import ru.yandex.practicum.filmorate.repository.mappers.FilmRowMapper;

import java.util.*;

@Repository
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class JdbcFilmRepository implements FilmRepository {


    private final NamedParameterJdbcOperations jdbc;
    private final GenreRepository genreRepository;
    private final FilmRowMapper mapper;
    private final FilmExtractor extractor;


    private static final String INSERT_QUERY = "INSERT INTO FILMS (\"name\", \"description\"," +
                                               " \"release_date\", \"duration\", \"rating\")" +
                                               " VALUES(:name, :description, :release_date, :duration, :rating);";
    private static final String GET_BY_ID_QUERY = "SELECT * FROM FILMS\n" +
                                                  "LEFT JOIN MPA_RATING mr ON mr.\"rating_id\" = \"rating\"\n" +
                                                  "LEFT JOIN FILM_GENRES fg ON fg.\"film_id\" = \"id\"\n" +
                                                  "LEFT JOIN GENRES g ON g.\"genre_id\" = fg.\"genre\"\n" +
                                                  "WHERE \"id\" = :id;";
    private static final String GET_ALL_QUERY = "SELECT * FROM FILMS\n" +
                                                "LEFT JOIN MPA_RATING mr ON mr.\"rating_id\" = \"rating\";";
    private static final String ADD_USER_LIKE_QUERY = "INSERT INTO FILM_LIKES (\"film_id\", \"user_id\") " +
                                                      "VALUES(:film_id, :user_id);";
    private static final String DELETE_USER_LIKE_QUERY = "DELETE FROM FILM_LIKES " +
                                                         "WHERE \"film_id\"=:film_id AND \"user_id\"=:user_id;";
    private static final String GET_MOST_POPULAR_FILMS_QUERY = "SELECT * FROM FILMS\n" +
                                                               "LEFT JOIN MPA_RATING mr ON mr.\"rating_id\" =\"id\"\n" +
                                                               "LEFT JOIN FILM_GENRES fg ON fg.\"film_id\" = \"id\" \n" +
                                                               "LEFT JOIN GENRES g ON g.\"genre_id\" = fg.\"genre\"\n" +
                                                               "WHERE \"id\" IN (SELECT \"film_id\" FROM FILM_LIKES\n" +
                                                               "GROUP BY \"film_id\"\n" +
                                                               "ORDER BY COUNT(\"film_id\") DESC)\n" +
                                                               "LIMIT :count;";
    private static final String UPDATE_FILM_QUERY = "UPDATE FILMS SET \"name\"=:name, \"description\"=:description," +
                                                    " \"release_date\"=:release_date, \"duration\"=:duration, \"rating\"=:rating" +
                                                    " WHERE \"id\"=:id;";

    @Override
    public Film save(Film film) {


        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("name", film.getName());
        mapSqlParameterSource.addValue("description", film.getDescription());
        mapSqlParameterSource.addValue("release_date", film.getReleaseDate());
        mapSqlParameterSource.addValue("duration", film.getDuration());
        mapSqlParameterSource.addValue("rating", film.getMpa().getId());

        jdbc.update(INSERT_QUERY, mapSqlParameterSource, keyHolder);
        film.setId(keyHolder.getKeyAs(Long.class));


        String insertFilmGenres = "INSERT INTO FILM_GENRES (\"film_id\", \"genre\")" +
                                  "VALUES(:film_id, :genre_id);";


        Set<Genre> filmGenres = film.getGenres();
        if (filmGenres != null) {
            MapSqlParameterSource[] sqlParameterSourceBatch = new MapSqlParameterSource[filmGenres.size()];
            int id = 0;
            for (Genre genre : filmGenres) {
                sqlParameterSourceBatch[id] = new MapSqlParameterSource();
                sqlParameterSourceBatch[id].addValue("film_id", film.getId());
                sqlParameterSourceBatch[id].addValue("genre_id", genre.getId());
                id++;
            }
            jdbc.batchUpdate(insertFilmGenres, sqlParameterSourceBatch);
        }

        return film;

    }

    @Override
    public void update(Film film) {

        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("name", film.getName());
        mapSqlParameterSource.addValue("description", film.getDescription());
        mapSqlParameterSource.addValue("release_date", film.getReleaseDate());
        mapSqlParameterSource.addValue("duration", film.getDuration());
        mapSqlParameterSource.addValue("rating", film.getMpa().getId());
        mapSqlParameterSource.addValue("id", film.getId());
        jdbc.update(UPDATE_FILM_QUERY, mapSqlParameterSource);

        String DELETE_QUERY = "DELETE FROM FILM_GENRES WHERE \"film_id\"=:film_id";
        mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("film_id", film.getId());
        jdbc.update(DELETE_QUERY, mapSqlParameterSource);

        String INSERT_GENRES_CONNECTION_QUERY = "INSERT INTO FILM_GENRES (\"film_id\", \"genre\") " +
                                                "VALUES(:film_id, :genre_id);";

        if (film.getGenres() != null) {
            MapSqlParameterSource[] batchMapSqlParameterSource = new MapSqlParameterSource[film.getGenres().size()];
            int id = 0;
            for (Genre genre : film.getGenres()) {
                batchMapSqlParameterSource[id] = new MapSqlParameterSource();
                batchMapSqlParameterSource[id].addValue("film_id", film.getId());
                batchMapSqlParameterSource[id].addValue("genre_id", genre.getId());
            }
            jdbc.batchUpdate(INSERT_GENRES_CONNECTION_QUERY, batchMapSqlParameterSource);
        }


    }

    @Override
    public List<Film> getAll() {
        List<Film> filmList = jdbc.query(GET_ALL_QUERY, mapper);

        for (Film film : filmList) {
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
            System.out.println(res);
            return Optional.ofNullable(res);
        } catch (EmptyResultDataAccessException ignored) {
            return Optional.empty();
        }

    }

    @Override
    public void addUserLike(long filmId, long userId) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("film_id", filmId);
        mapSqlParameterSource.addValue("user_id", userId);

        jdbc.update(ADD_USER_LIKE_QUERY, mapSqlParameterSource);
    }

    @Override
    public void deleteUserLike(long filmId, long userId) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("film_id", filmId);
        mapSqlParameterSource.addValue("user_id", userId);

        jdbc.update(DELETE_USER_LIKE_QUERY, mapSqlParameterSource);
    }

    @Override
    public List<Film> getMostPopularFilms(int count) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("count", count);
        List<Film> popularList = jdbc.query(GET_MOST_POPULAR_FILMS_QUERY, mapSqlParameterSource, mapper);
        for (Film film : popularList) {
            List<Genre> genreList = genreRepository.getByFilmId(film.getId());
            film.setGenres(new LinkedHashSet<>(genreRepository.getByFilmId(film.getId())));
        }

        return popularList;
    }

}
