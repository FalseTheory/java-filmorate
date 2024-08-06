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

        return userService.get(userId);
    }
    @PutMapping("/{userId}/friends/{friendId}")
    public void putFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        userService.addFriend(userId, friendId);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public void deleteFromFriends(@PathVariable Long userId, @PathVariable Long friendId) {
        userService.deleteFriend(userId, friendId);
    }

    @GetMapping("/{userId}/friends")
    public List<User> getUserFriends(@PathVariable Long userId) {
        return userService.getFriendsList(userId);
    }

    @GetMapping("/{userId}/friends/common/{otherId}")
    public List<User> getUserCommonFriendsWithOtherUser(@PathVariable Long userId, @PathVariable Long otherId) {
        return null;
    }

    @GetMapping
    public Collection<User> findAll() {
        return userService.findAll();
    }

    @PostMapping
    public User save(@Valid @RequestBody User user) {

        return userService.save(user);

    }

    @PutMapping
    public User update(@Validated(OnUpdate.class) @RequestBody User newUser) {

        return userService.update(newUser);

    }

}
