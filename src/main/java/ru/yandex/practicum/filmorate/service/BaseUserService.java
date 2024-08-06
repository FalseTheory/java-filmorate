package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.user.UserRepository;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BaseUserService implements UserService{

    private final UserRepository userRepository;

    @Override
    public User get(long userId) {
        return userRepository.get(userId)
                .orElseThrow(() -> new NotFoundException("User not found with " + userId));
    }

    @Override
    public User save(User user) {

        userRepository.create(user);
        return user;
    }

    @Override
    public User update(User newUser) {

        return userRepository.update(newUser);
    }

    @Override
    public void addFriend(long userId, long friendId) {
        User user = userRepository.get(userId)
                .orElseThrow(() -> new NotFoundException("User not found with " + userId));
        User friend = userRepository.get(friendId)
                .orElseThrow(() -> new NotFoundException("User not found with " + friendId));

        userRepository.addFriend(userId, friendId);

    }

    @Override
    public void deleteFriend(long userId, long friendId) {
        User user = userRepository.get(userId)
                .orElseThrow(() -> new NotFoundException("User not found with " + userId));

        userRepository.deleteFriend(userId, friendId);
    }

    @Override
    public Collection<User> findAll() {
        return userRepository.getAll();
    }

    @Override
    public List<User> getFriendsList(long userId) {
        return userRepository.returnFriendsList(userId);
    }
}
