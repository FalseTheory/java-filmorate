package ru.yandex.practicum.filmorate.repository.user;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.mappers.UserExtractor;
import ru.yandex.practicum.filmorate.repository.mappers.UserRowMapper;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcUserRepository implements UserRepository {
    private final NamedParameterJdbcOperations jdbc;
    private final UserRowMapper mapper;
    private final UserExtractor extractor;


    private static final String GET_ALL_QUERY = "SELECT * FROM USERS";
    private static final String GET_BY_ID_QUERY = "SELECT * FROM USERS\n" +
                                                  "WHERE \"id\" = :id;";
    private static final String UPDATE_QUERY = "UPDATE USERS SET \"email\"=:email, \"login\"=:login," +
                                               " \"name\"=:name, \"birthday\"=:birthday " +
                                               "WHERE \"id\"=:id;";
    private static final String INSERT_QUERY = "INSERT INTO USERS (\"email\", \"login\", \"name\", \"birthday\")" +
                                               " VALUES(:email, :login, :name, :birthday);";
    private static final String ADD_FRIEND_QUERY = "INSERT INTO FRIENDS (\"user_id\", \"friend_id\")" +
                                                   " VALUES(:user_id, :friend_id);";
    private static final String DELETE_FRIEND_QUERY = "DELETE FROM FRIENDS " +
                                                      "WHERE \"user_id\"=:user_id AND \"friend_id\"=:friend_id;";
    private static final String GET_USER_FRIENDS_LIST_QUERY = "SELECT * FROM USERS\n" +
                                                              "WHERE \"id\" IN (SELECT \"friend_id\" FROM FRIENDS f\n" +
                                                              "WHERE \"user_id\"=:user_id);";
    private static final String GET_COMMON_FRIENDS_LIST_QUERY = "SELECT * FROM USERS \n" +
                                                                "WHERE \"id\" IN(SELECT \"friend_id\" FROM FRIENDS\n" +
                                                                "WHERE \"user_id\"=:user_id) AND \"id\" IN (SELECT \"friend_id\" FROM FRIENDS\n" +
                                                                "WHERE \"user_id\"=:other_id);";

    @Override
    public User create(User user) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();

        mapSqlParameterSource.addValue("email", user.getEmail());
        mapSqlParameterSource.addValue("login", user.getLogin());
        mapSqlParameterSource.addValue("name", user.getName());
        mapSqlParameterSource.addValue("birthday", user.getBirthday());

        jdbc.update(INSERT_QUERY, mapSqlParameterSource, keyHolder);
        user.setId(keyHolder.getKeyAs(Long.class));

        return user;
    }

    @Override
    public void update(User user) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("email", user.getEmail());
        mapSqlParameterSource.addValue("login", user.getLogin());
        mapSqlParameterSource.addValue("name", user.getName());
        mapSqlParameterSource.addValue("birthday", user.getBirthday());
        mapSqlParameterSource.addValue("id", user.getId());

        jdbc.update(UPDATE_QUERY, mapSqlParameterSource);

    }

    @Override
    public Optional<User> get(long userId) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("id", userId);
        try {
            User res = jdbc.query(GET_BY_ID_QUERY, mapSqlParameterSource, extractor);
            return Optional.ofNullable(res);
        } catch (EmptyResultDataAccessException ignored) {
            return Optional.empty();
        }

    }

    @Override
    public List<User> getAll() {
        return jdbc.query(GET_ALL_QUERY, mapper);
    }

    @Override
    public void addFriend(long userId, long friendId) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("user_id", userId);
        mapSqlParameterSource.addValue("friend_id", friendId);

        jdbc.update(ADD_FRIEND_QUERY, mapSqlParameterSource);
    }

    @Override
    public void deleteFriend(long userId, long friendId) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("user_id", userId);
        mapSqlParameterSource.addValue("friend_id", friendId);

        jdbc.update(DELETE_FRIEND_QUERY, mapSqlParameterSource);
    }

    @Override
    public List<User> returnFriendsList(long userId) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("user_id", userId);
        return jdbc.query(GET_USER_FRIENDS_LIST_QUERY, mapSqlParameterSource, mapper);
    }

    @Override
    public List<User> returnCommonFriends(long userId, long otherId) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("user_id", userId);
        mapSqlParameterSource.addValue("other_id", otherId);

        return jdbc.query(GET_COMMON_FRIENDS_LIST_QUERY, mapSqlParameterSource, mapper);
    }
}
