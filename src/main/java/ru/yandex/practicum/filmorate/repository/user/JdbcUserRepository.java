package ru.yandex.practicum.filmorate.repository.user;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.mappers.UserExtractor;
import ru.yandex.practicum.filmorate.repository.mappers.UserRowMapper;

import java.util.Collection;
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
    private static final String UPDATE_QUERY = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE id = ?";
    private static final String INSERT_QUERY = "INSERT INTO users(email, login, name, birthday)" +
            " VALUES(?,?,?,?) returning id";
    private static final String ADD_FRIEND_QUERY = "";
    private static final String DELETE_FRIEND_QUERY = "";
    private static final String GET_USER_FRIENDS_LIST_QUERY = "";
    private static final String GET_COMMON_FRIENDS_LIST_QUERY = "";

    @Override
    public User create(User user) {

        return user;
    }

    @Override
    public void update(User user) {

    }

    @Override
    public Optional<User> get(long userId) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("id", userId);
        try{
            User res = jdbc.query(GET_BY_ID_QUERY, mapSqlParameterSource, extractor);
            return Optional.ofNullable(res);
        } catch(EmptyResultDataAccessException ignored) {
            return Optional.empty();
        }

    }

    @Override
    public List<User> getAll() {
        return jdbc.query(GET_ALL_QUERY, mapper);
    }

    @Override
    public void addFriend(long userId, long friendId) {

    }

    @Override
    public void deleteFriend(long userId, long friendId) {

    }

    @Override
    public List<User> returnFriendsList(long userId) {
        return List.of();
    }

    @Override
    public List<User> returnCommonFriends(long userId, long otherId) {
        return List.of();
    }
}
