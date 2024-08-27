package ru.yandex.practicum.filmorate.repository.film;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
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


    @Override
    public List<Genre> getByIds(List<Long> ids) {

        StringBuilder getByIdsQuery = new StringBuilder("SELECT * FROM GENRES\n" +
                                                        "WHERE \"genre_id\" IN (");


        for (int i = 0; i < ids.size(); i++) {
            if (i == ids.size() - 1) {
                getByIdsQuery.append(ids.get(i));
            } else {
                getByIdsQuery.append(ids.get(i)).append(",");
            }

        }

        getByIdsQuery.append(");");

        return jdbc.query(getByIdsQuery.toString(), (rs, rowNum) -> {
            Genre genre = new Genre();
            genre.setId(rs.getLong("genre_id"));
            genre.setName(rs.getString("genre_name"));
            return genre;
        });
    }

    @Override
    public Optional<Genre> getById(long id) {

        final String getByIdQuery = "SELECT * FROM GENRES WHERE \"genre_id\" = :genre_id;";

        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("genre_id", id);

        Genre res = jdbc.query(getByIdQuery, mapSqlParameterSource, new GenreExtractor());
        return Optional.ofNullable(res);

    }


    @Override
    public List<Genre> getAll() {

        final String getAllQuery = "SELECT * FROM GENRES" +
                                   " ORDER BY \"genre_id\"";

        return jdbc.query(getAllQuery, (rs, rowNum) -> {
            Genre genre = new Genre();
            genre.setId(rs.getLong("genre_id"));
            genre.setName(rs.getString("genre_name"));
            return genre;
        });
    }

    private static class GenreExtractor implements ResultSetExtractor<Genre> {

        @Override
        public Genre extractData(ResultSet rs) throws SQLException, DataAccessException {
            Genre genre = null;
            if (rs.next()) {
                genre = new Genre();
                genre.setId(rs.getLong("genre_id"));
                genre.setName(rs.getString("genre_name"));
            }
            return genre;
        }
    }
}
