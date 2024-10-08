package ru.yandex.practicum.filmorate.repository.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    User create(User user);

    void update(User user);

    Optional<User> get(long userId);

    List<User> getAll();

    void addFriend(long userId, long friendId);

    void deleteFriend(long userId, long friendId);

    List<User> returnFriendsList(long userId);

    List<User> returnCommonFriends(long userId, long otherId);


}
