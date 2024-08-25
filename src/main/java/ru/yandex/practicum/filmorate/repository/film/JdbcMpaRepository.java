package ru.yandex.practicum.filmorate.repository.film;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPA;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcMpaRepository implements MpaRepository {

    private final NamedParameterJdbcOperations jdbc;
    private static final String GET_BY_ID_QUERY = "SELECT * FROM MPA_RATING WHERE \"rating_id\" = :rating_id;";

    @Override
    public Optional<MPA> getById(long id) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("rating_id", id);
        try {
            MPA res = jdbc.query(GET_BY_ID_QUERY, mapSqlParameterSource, new MpaExtractor());
            return Optional.ofNullable(res);
        } catch (EmptyResultDataAccessException ignored) {
            return Optional.empty();
        }
    }

    private class MpaExtractor implements ResultSetExtractor<MPA> {

        @Override
        public MPA extractData(ResultSet rs) throws SQLException, DataAccessException {
            MPA mpa = null;
            while(rs.next()){
                mpa = new MPA(
                        rs.getLong("rating_id"),
                        rs.getString("mpa_name")
                );
            }
            return mpa;
        }
    }
}
