package ru.yandex.practicum.filmorate.repository.user;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository("dbUserRepository")
@RequiredArgsConstructor
public class JdbcUserRepository implements UserRepository {
    private final NamedParameterJdbcOperations jdbc;


    private static final String GET_ALL_QUERY = "SELECT * FROM users";
    private static final String GET_BY_ID_QUERY = "SELECT * FROM users WHERE id = ?";
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
        return Optional.empty();
    }

    @Override
    public Collection<User> getAll() {
        return List.of();
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
