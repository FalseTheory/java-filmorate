package ru.yandex.practicum.filmorate.repository.user;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcUserRepository implements UserRepository {
    private final NamedParameterJdbcOperations jdbc;

    private final UserRowMapper mapper = new UserRowMapper();
    private final UserExtractor extractor = new UserExtractor();


    @Override
    public User create(User user) {


        final String insertQuery = "INSERT INTO USERS (\"email\", \"login\", \"name\", \"birthday\")" +
                                   " VALUES(:email, :login, :name, :birthday);";

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();

        mapSqlParameterSource.addValue("email", user.getEmail());
        mapSqlParameterSource.addValue("login", user.getLogin());
        mapSqlParameterSource.addValue("name", user.getName());
        mapSqlParameterSource.addValue("birthday", user.getBirthday());

        jdbc.update(insertQuery, mapSqlParameterSource, keyHolder);
        user.setId(keyHolder.getKeyAs(Long.class));

        return user;
    }

    @Override
    public void update(User user) {
        final String updateQuery = "UPDATE USERS SET \"email\"=:email, \"login\"=:login," +
                                   " \"name\"=:name, \"birthday\"=:birthday " +
                                   "WHERE \"id\"=:id;";

        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("email", user.getEmail());
        mapSqlParameterSource.addValue("login", user.getLogin());
        mapSqlParameterSource.addValue("name", user.getName());
        mapSqlParameterSource.addValue("birthday", user.getBirthday());
        mapSqlParameterSource.addValue("id", user.getId());

        jdbc.update(updateQuery, mapSqlParameterSource);

    }

    @Override
    public Optional<User> get(long userId) {

        final String getByIdQuery = "SELECT * FROM USERS\n" +
                                    "WHERE \"id\" = :id;";

        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("id", userId);
        User res = jdbc.query(getByIdQuery, mapSqlParameterSource, extractor);
        return Optional.ofNullable(res);


    }

    @Override
    public List<User> getAll() {

        final String getAllQuery = "SELECT * FROM USERS";

        return jdbc.query(getAllQuery, mapper);
    }

    @Override
    public void addFriend(long userId, long friendId) {
        final String addFriendQuery = "MERGE INTO FRIENDS (\"user_id\", \"friend_id\")" +
                                      " VALUES(:user_id, :friend_id);";

        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("user_id", userId);
        mapSqlParameterSource.addValue("friend_id", friendId);

        jdbc.update(addFriendQuery, mapSqlParameterSource);
    }

    @Override
    public void deleteFriend(long userId, long friendId) {
        final String deleteFriendQuery = "DELETE FROM FRIENDS " +
                                         "WHERE \"user_id\"=:user_id AND \"friend_id\"=:friend_id;";

        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("user_id", userId);
        mapSqlParameterSource.addValue("friend_id", friendId);

        jdbc.update(deleteFriendQuery, mapSqlParameterSource);
    }

    @Override
    public List<User> returnFriendsList(long userId) {
        final String getUserFriendsListQuery = "SELECT * FROM USERS\n" +
                                               "WHERE \"id\" IN (SELECT \"friend_id\" FROM FRIENDS f\n" +
                                               "WHERE \"user_id\"=:user_id);";

        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("user_id", userId);
        return jdbc.query(getUserFriendsListQuery, mapSqlParameterSource, mapper);
    }

    @Override
    public List<User> returnCommonFriends(long userId, long otherId) {

        final String getCommonFriendsListQuery = "SELECT * FROM USERS \n" +
                                                 "WHERE \"id\" IN(SELECT \"friend_id\" FROM FRIENDS\n" +
                                                 "WHERE \"user_id\"=:user_id) AND" +
                                                 " \"id\" IN (SELECT \"friend_id\" FROM FRIENDS\n" +
                                                 "WHERE \"user_id\"=:other_id);";

        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("user_id", userId);
        mapSqlParameterSource.addValue("other_id", otherId);

        return jdbc.query(getCommonFriendsListQuery, mapSqlParameterSource, mapper);
    }

    private static class UserExtractor implements ResultSetExtractor<User> {
        @Override
        public User extractData(ResultSet rs) throws SQLException, DataAccessException {
            User user = null;
            if (rs.next()) {
                user = new User();
                user.setId(rs.getLong("id"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setLogin(rs.getString("login"));
                user.setBirthday(rs.getDate("birthday").toLocalDate());

            }


            return user;
        }
    }

    private static class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            User user = new User();
            user.setId(resultSet.getLong("id"));
            user.setEmail(resultSet.getString("email"));
            user.setLogin(resultSet.getString("login"));
            user.setName(resultSet.getString("name"));
            user.setBirthday(resultSet.getDate("birthday").toLocalDate());

            return user;
        }
    }
}
