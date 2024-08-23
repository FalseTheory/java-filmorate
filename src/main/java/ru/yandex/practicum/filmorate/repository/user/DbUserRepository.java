package ru.yandex.practicum.filmorate.repository.user;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.BaseDbRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository("dbUserRepository")
public class DbUserRepository extends BaseDbRepository<User> implements UserRepository {

    public DbUserRepository(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public User create(User user) {
        return null;
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
