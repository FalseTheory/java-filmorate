package ru.yandex.practicum.filmorate.repository.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
public class InMemoryUserRepository implements UserRepository {

    Map<Long, User> users = new HashMap<>();

    Set<String> userMails = new HashSet<>();

    Map<Long, Set<Long>> friendsLists = new HashMap<>();

    private Long idCount = 0L;

    @Override
    public User create(User user) {

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (!userMails.add(user.getEmail())) {
            throw new ConditionsNotMetException("Этот email уже используется");
        }
        user.setId(++idCount);
        users.put(user.getId(), user);
        friendsLists.put(user.getId(), new HashSet<>());

        return user;
    }

    @Override
    public User update(User newUser) {
        User oldUser = users.get(newUser.getId());
        if (oldUser == null) {
            throw new NotFoundException("Пользователь с id = " + newUser.getId() + " не найден");
        }
        if (!oldUser.getEmail().equals(newUser.getEmail())) {
            if (!userMails.add(newUser.getEmail())) {
                throw new ConditionsNotMetException("Этот email уже используется");
            }
            userMails.remove(oldUser.getEmail());
            oldUser.setEmail(newUser.getEmail());
        }
        oldUser.setLogin(newUser.getLogin());
        oldUser.setBirthday(newUser.getBirthday());
        oldUser.setName(newUser.getName());
        return oldUser;
    }

    @Override
    public Optional<User> get(long userId) {
        return Optional.ofNullable(users.get(userId));
    }


    @Override
    public Collection<User> getAll() {
        return users.values();
    }

    @Override
    public void addFriend(long userId, long friendId) {

        friendsLists.get(userId).add(friendId);
        friendsLists.get(friendId).add(userId);

    }

    @Override
    public List<User> returnFriendsList(long userId) {

        return users.values().stream()
                .filter(user -> friendsLists.get(userId).contains(user.getId()))
                .toList();
    }

    @Override
    public List<User> returnCommonFriends(long userId, long otherId) {

        return friendsLists.get(userId)
                .stream()
                .filter(id -> friendsLists.get(otherId).contains(id))
                .map(id -> users.get(id))
                .toList();
    }

    @Override
    public void deleteFriend(long userId, long friendId) {
        friendsLists.get(userId).remove(friendId);
        friendsLists.get(friendId).remove(userId);
    }


}
