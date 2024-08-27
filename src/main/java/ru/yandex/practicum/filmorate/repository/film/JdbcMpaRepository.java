package ru.yandex.practicum.filmorate.repository.film;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcMpaRepository implements MpaRepository {

    private final NamedParameterJdbcOperations jdbc;


    @Override
    public Optional<Mpa> getById(long id) {

        final String getByIdQuery = "SELECT * FROM MPA_RATING WHERE \"rating_id\" = :rating_id;";

        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("rating_id", id);
        Mpa res = jdbc.query(getByIdQuery, mapSqlParameterSource, new MpaExtractor());
        return Optional.ofNullable(res);

    }

    @Override
    public List<Mpa> getAll() {

        final String getAllQuery = "SELECT * FROM MPA_RATING" +
                                   " ORDER BY \"rating_id\"";

        return jdbc.query(getAllQuery, (rs, rowNum) -> {
            Mpa mpa = new Mpa();
            mpa.setId(rs.getLong("rating_id"));
            mpa.setName(rs.getString("mpa_name"));
            return mpa;
        });
    }

    private static class MpaExtractor implements ResultSetExtractor<Mpa> {

        @Override
        public Mpa extractData(ResultSet rs) throws SQLException, DataAccessException {
            Mpa mpa = null;
            if (rs.next()) {
                mpa = new Mpa();
                mpa.setId(rs.getLong("rating_id"));
                mpa.setName(rs.getString("mpa_name"));
            }
            return mpa;
        }
    }
}
