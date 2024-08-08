package ru.yandex.practicum.filmorate.repository.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryUserRepository implements UserRepository {

    Map<Long, User> users = new HashMap<>();

    Set<String> userMails = new HashSet<>();

    Map<Long, Set<Long>> friendsLists = new HashMap<>();

    private Long idCount = 0L;

    @Override
    public User create(User user) {

        if (!userMails.add(user.getEmail())) {
            throw new ConditionsNotMetException("Этот email уже используется");
        }
        user.setId(++idCount);
        users.put(user.getId(), user);
        friendsLists.put(user.getId(), new HashSet<>());

        return user;
    }

    @Override
    public void update(User user) {

        User oldUser = users.get(user.getId());

        if (!oldUser.getEmail().equals(user.getEmail())) {
            if (!userMails.add(user.getEmail())) {
                throw new ConditionsNotMetException("Этот email уже используется");
            }
            userMails.remove(oldUser.getEmail());
            oldUser.setEmail(user.getEmail());
        }
        oldUser.setLogin(user.getLogin());
        oldUser.setBirthday(user.getBirthday());
        oldUser.setName(user.getName());

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

        return friendsLists.get(userId).stream()
                .map(users::get)
                .collect(Collectors.toList());
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
