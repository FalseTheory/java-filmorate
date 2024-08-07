package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.validators.OnUpdate;

import java.util.*;

@RestController
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {


    private final UserService userService;


    @GetMapping("/{userId}")
    public User get(@PathVariable Long userId) {
        log.info("Get user by id = {}", userId);
        return userService.get(userId);
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public void putFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        log.info("Put friend with id = {} to user = {}", friendId, userId);
        userService.addFriend(userId, friendId);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public void deleteFromFriends(@PathVariable Long userId, @PathVariable Long friendId) {
        log.info("Delete friend with id = {} from user = {}", friendId, userId);
        userService.deleteFriend(userId, friendId);
    }

    @GetMapping("/{userId}/friends")
    public List<User> getUserFriends(@PathVariable Long userId) {
        log.info("Get user - {} friends", userId);
        return userService.getFriendsList(userId);
    }

    @GetMapping("/{userId}/friends/common/{otherId}")
    public List<User> getUserCommonFriendsWithOtherUser(@PathVariable Long userId, @PathVariable Long otherId) {
        log.info("Get user = {} common friends with other user with id = {}", userId, otherId);
        return userService.getCommonFriends(userId, otherId);
    }

    @GetMapping
    public Collection<User> findAll() {
        log.info("Get all users");
        return userService.findAll();
    }

    @PostMapping
    public User save(@Valid @RequestBody User user) {
        log.info("Save user - {}", user.toString());
        return userService.save(user);

    }

    @PutMapping
    public User update(@Validated(OnUpdate.class) @RequestBody User newUser) {
        log.info("Update user with id - {}, and body - {}", newUser.getId(), newUser.toString());
        return userService.update(newUser);

    }

}
