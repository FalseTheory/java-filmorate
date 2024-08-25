package ru.yandex.practicum.filmorate.repository.film;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcGenreRepository implements GenreRepository {

    private final NamedParameterJdbcOperations jdbc;
    private static final String GET_BY_ID_QUERY = "SELECT * FROM GENRES WHERE \"genre_id\" = :genre_id;";
    private static final String GET_ALL_GENRES_FOR_FILM_BY_ID = "SELECT * FROM GENRES\n" +
                                                                "WHERE \"genre_id\" IN (\n" +
                                                                "SELECT \"genre\" FROM FILM_GENRES\n" +
                                                                "WHERE \"film_id\"=:film_id);";
    private static final String GET_ALL_QUERY = "SELECT * FROM GENRES" +
                                                " ORDER BY \"genre_id\"";


    @Override
    public List<Genre> getByIds(List<Long> ids) {
        return List.of();
    }

    @Override
    public Optional<Genre> getById(long id) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("genre_id", id);
        try {
            Genre res = jdbc.query(GET_BY_ID_QUERY, mapSqlParameterSource, new GenreExtractor());
            return Optional.ofNullable(res);
        } catch (EmptyResultDataAccessException ignored) {
            return Optional.empty();
        }
    }

    @Override
    public List<Genre> getByFilmId(long id) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("film_id", id);
        return jdbc.query(GET_ALL_GENRES_FOR_FILM_BY_ID, mapSqlParameterSource, (rs, rowNum) -> {
            Genre genre = new Genre();
            genre.setId(rs.getLong("genre_id"));
            genre.setName(rs.getString("genre_name"));
            return genre;
        });
    }

    @Override
    public List<Genre> getAll() {
        return jdbc.query(GET_ALL_QUERY, new RowMapper<Genre>() {
            @Override
            public Genre mapRow(ResultSet rs, int rowNum) throws SQLException {
                Genre genre = new Genre();
                genre.setId(rs.getLong("genre_id"));
                genre.setName(rs.getString("genre_name"));
                return genre;
            }
        });
    }

    private class GenreExtractor implements ResultSetExtractor<Genre> {

        @Override
        public Genre extractData(ResultSet rs) throws SQLException, DataAccessException {
            Genre genre = null;
            while (rs.next()) {
                genre = new Genre();
                genre.setId(rs.getLong("genre_id"));
                genre.setName(rs.getString("genre_name"));
            }
            return genre;
        }
    }
}
