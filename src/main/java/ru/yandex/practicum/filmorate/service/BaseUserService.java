package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.user.UserRepository;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BaseUserService implements UserService {

    private final UserRepository userRepository;

    @Override
    public User get(long userId) {
        return userRepository.get(userId)
                .orElseThrow(() -> new NotFoundException("User not found with " + userId));
    }

    @Override
    public User save(User user) {

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return userRepository.create(user);

    }

    @Override
    public User update(User user) {

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        userRepository.update(user);
        return userRepository.get(user.getId())
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + user.getId() + " не найден"));
    }

    @Override
    public void addFriend(long userId, long friendId) {
        User user = userRepository.get(userId)
                .orElseThrow(() -> new NotFoundException("User not found with " + userId));
        User friend = userRepository.get(friendId)
                .orElseThrow(() -> new NotFoundException("User not found with " + friendId));


        userRepository.addFriend(user.getId(), friend.getId());

    }

    @Override
    public void deleteFriend(long userId, long friendId) {
        User user = userRepository.get(userId)
                .orElseThrow(() -> new NotFoundException("User not found with " + userId));
        User friend = userRepository.get(friendId)
                .orElseThrow(() -> new NotFoundException("User not found with " + friendId));

        userRepository.deleteFriend(user.getId(), friend.getId());
    }

    @Override
    public Collection<User> findAll() {
        return userRepository.getAll();
    }

    @Override
    public List<User> getFriendsList(long userId) {
        User user = userRepository.get(userId)
                .orElseThrow(() -> new NotFoundException("User not found with " + userId));

        return userRepository.returnFriendsList(user.getId());
    }

    @Override
    public List<User> getCommonFriends(long userId, long otherId) {

        User user = userRepository.get(userId)
                .orElseThrow(() -> new NotFoundException("User not found with " + userId));
        User other = userRepository.get(otherId)
                .orElseThrow(() -> new NotFoundException("User not found with " + otherId));

        return userRepository.returnCommonFriends(user.getId(), other.getId());

    }
}
