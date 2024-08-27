package ru.yandex.practicum.filmorate.repository.film;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class JdbcFilmRepository implements FilmRepository {


    private final NamedParameterJdbcOperations jdbc;


    private final FilmExtractor extractor = new FilmExtractor();
    private final FilmRowExtractor rowExtractor = new FilmRowExtractor();


    @Override
    public Film save(Film film) {


        final String insertQuery = "INSERT INTO FILMS (\"name\", \"description\"," +
                                   " \"release_date\", \"duration\", \"rating\")" +
                                   " VALUES(:name, :description, :release_date, :duration, :rating);";

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("name", film.getName());
        mapSqlParameterSource.addValue("description", film.getDescription());
        mapSqlParameterSource.addValue("release_date", film.getReleaseDate());
        mapSqlParameterSource.addValue("duration", film.getDuration());
        mapSqlParameterSource.addValue("rating", film.getMpa().getId());

        jdbc.update(insertQuery, mapSqlParameterSource, keyHolder);
        film.setId(keyHolder.getKeyAs(Long.class));


        updateFilmGenre(film);

        return film;

    }

    @Override
    public void update(Film film) {

        final String updateQuery = "UPDATE FILMS SET \"name\"=:name, \"description\"=:description," +
                                   " \"release_date\"=:release_date, \"duration\"=:duration, \"rating\"=:rating" +
                                   " WHERE \"id\"=:id;";

        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("name", film.getName());
        mapSqlParameterSource.addValue("description", film.getDescription());
        mapSqlParameterSource.addValue("release_date", film.getReleaseDate());
        mapSqlParameterSource.addValue("duration", film.getDuration());
        mapSqlParameterSource.addValue("rating", film.getMpa().getId());
        mapSqlParameterSource.addValue("id", film.getId());
        jdbc.update(updateQuery, mapSqlParameterSource);

        String deleteQuery = "DELETE FROM FILM_GENRES WHERE \"film_id\"=:film_id";
        mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("film_id", film.getId());
        jdbc.update(deleteQuery, mapSqlParameterSource);


        updateFilmGenre(film);


    }

    @Override
    public List<Film> getAll() {

        final String getAllQuery = "SELECT * FROM FILMS\n" +
                                   "LEFT JOIN MPA_RATING mr ON mr.\"rating_id\" = \"rating\"\n" +
                                   "LEFT JOIN FILM_GENRES fg ON fg.\"film_id\" = \"id\"\n" +
                                   "LEFT JOIN GENRES g ON g.\"genre_id\" = fg.\"genre\"\n";

        return jdbc.query(getAllQuery, rowExtractor);
    }

    @Override
    public Optional<Film> get(long filmId) {
        final String getByIdQuery = "SELECT * FROM FILMS\n" +
                                    "LEFT JOIN MPA_RATING mr ON mr.\"rating_id\" = \"rating\"\n" +
                                    "LEFT JOIN FILM_GENRES fg ON fg.\"film_id\" = \"id\"\n" +
                                    "LEFT JOIN GENRES g ON g.\"genre_id\" = fg.\"genre\"\n" +
                                    "WHERE \"id\" = :id;";

        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("id", filmId);

        Film res = jdbc.query(getByIdQuery, mapSqlParameterSource, extractor);
        return Optional.ofNullable(res);

    }

    @Override
    public void addUserLike(long filmId, long userId) {

        final String addUserLikeQuery = "MERGE INTO FILM_LIKES (\"film_id\", \"user_id\") " +
                                        "VALUES(:film_id, :user_id);";

        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("film_id", filmId);
        mapSqlParameterSource.addValue("user_id", userId);

        jdbc.update(addUserLikeQuery, mapSqlParameterSource);
    }

    @Override
    public void deleteUserLike(long filmId, long userId) {
        final String deleteUserLikeQuery = "DELETE FROM FILM_LIKES " +
                                           "WHERE \"film_id\"=:film_id AND \"user_id\"=:user_id;";

        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("film_id", filmId);
        mapSqlParameterSource.addValue("user_id", userId);

        jdbc.update(deleteUserLikeQuery, mapSqlParameterSource);
    }

    @Override
    public List<Film> getMostPopularFilms(int count) {

        final String getTopPopularFilmsQuery = "SELECT * FROM FILMS\n" +
                                               "LEFT JOIN MPA_RATING mr ON mr.\"rating_id\" =\"rating\"\n" +
                                               "LEFT JOIN FILM_GENRES fg ON fg.\"film_id\" = \"id\"\n" +
                                               "LEFT JOIN GENRES g ON g.\"genre_id\" = fg.\"genre\"\n" +
                                               "WHERE \"id\" IN (SELECT \"film_id\" FROM FILM_LIKES\n" +
                                               "GROUP BY \"film_id\"\n" +
                                               "ORDER BY COUNT(\"film_id\") DESC)\n" +
                                               "LIMIT :count;";

        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("count", count);


        return jdbc.query(getTopPopularFilmsQuery, mapSqlParameterSource, rowExtractor);

    }


    private static class FilmRowExtractor implements ResultSetExtractor<List<Film>> {

        @Override
        public List<Film> extractData(ResultSet rs) throws SQLException, DataAccessException {
            List<Film> filmList = new ArrayList<>();
            Film film = null;
            long currentId = 0;
            while (rs.next()) {
                if (currentId != rs.getLong("id")) {
                    filmList.add(film);
                    film = null;
                }
                if (film == null) {
                    film = new Film();
                    film.setId(rs.getLong("id"));
                    film.setName(rs.getString("name"));
                    film.setDescription(rs.getString("description"));
                    film.setReleaseDate(rs.getDate("release_date").toLocalDate());
                    film.setDuration(rs.getInt("duration"));
                    Mpa mpa = new Mpa();
                    mpa.setId(rs.getLong("rating_id"));
                    mpa.setName(rs.getString("mpa_name"));
                    film.setMpa(mpa);
                    film.setGenres(new LinkedHashSet<>());
                }
                long genreId = rs.getLong("genre_id");
                if (genreId > 0) {
                    Genre genre = new Genre();
                    genre.setId(rs.getLong("genre_id"));
                    genre.setName(rs.getString("genre_name"));
                    film.getGenres().add(genre);
                }
                currentId = rs.getLong("id");
            }
            filmList.add(film);
            filmList.removeFirst();
            return filmList;
        }
    }

    private void updateFilmGenre(Film film) {
        Set<Genre> genres = film.getGenres();
        if (genres != null) {
            MapSqlParameterSource[] sqlParameterSourceBatch = new MapSqlParameterSource[genres.size()];

            String insertGenresConnectionQuery = "MERGE INTO FILM_GENRES (\"film_id\", \"genre\") " +
                                                 "VALUES(:film_id, :genre_id);";
            int id = 0;
            for (Genre genre : genres) {
                sqlParameterSourceBatch[id] = new MapSqlParameterSource();
                sqlParameterSourceBatch[id].addValue("film_id", film.getId());
                sqlParameterSourceBatch[id].addValue("genre_id", genre.getId());
                id++;
            }
            jdbc.batchUpdate(insertGenresConnectionQuery, sqlParameterSourceBatch);
        }

    }


    private static class FilmExtractor implements ResultSetExtractor<Film> {
        @Override
        public Film extractData(ResultSet rs) throws SQLException, DataAccessException {
            Film film = null;

            while (rs.next()) {
                if (film == null) {
                    film = new Film();
                    film.setId(rs.getLong("id"));
                    film.setName(rs.getString("name"));
                    film.setDescription(rs.getString("description"));
                    film.setReleaseDate(rs.getDate("release_date").toLocalDate());
                    film.setDuration(rs.getInt("duration"));
                    Mpa mpa = new Mpa();
                    mpa.setId(rs.getLong("rating_id"));
                    mpa.setName(rs.getString("mpa_name"));
                    film.setMpa(mpa);
                    film.setGenres(new LinkedHashSet<>());
                }
                long genreId = rs.getLong("genre_id");
                if (genreId > 0) {
                    Genre genre = new Genre();
                    genre.setId(rs.getLong("genre_id"));
                    genre.setName(rs.getString("genre_name"));
                    film.getGenres().add(genre);
                }

            }
            return film;
        }
    }

}
