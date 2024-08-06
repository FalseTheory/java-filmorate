package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

public interface UserService {

    User get(long userId);

    User save(User user);

    User update(User newUser);

    void addFriend(long userId, long friendId);

    void deleteFriend(long userId, long friendId);

    Collection<User> findAll();

    List<User> getFriendsList(long userId);

    List<User> getCommonFriends(long userId, long otherId);


}
