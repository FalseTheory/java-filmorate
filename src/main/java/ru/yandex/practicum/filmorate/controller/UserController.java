package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validators.OnUpdate;

import java.util.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private Long idCount = 0L;

    Map<Long, User> users = new HashMap<>();

    Set<String> userMails = new HashSet<>();


    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("trying to create new user record");
        log.debug("parsed user record - {}", user);

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (!userMails.add(user.getEmail())) {
            throw new ConditionsNotMetException("Этот email уже используется");
        }
        log.trace("calculating new id");
        user.setId(++idCount);
        users.put(user.getId(), user);
        log.info("Successfully created user - {}", user);


        return user;

    }

    @PutMapping
    public User update(@Validated(OnUpdate.class) @RequestBody User newUser) {
        log.info("trying to update user record");

        log.debug("updating user with id - {}", newUser.getId());
        User oldUser = users.get(newUser.getId());
        if (oldUser == null) {
            throw new NotFoundException("Пользователь с id = " + newUser.getId() + " не найден");
        }
        log.debug("old user record - {}", oldUser);
        log.debug("new user record - {}", newUser);

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

        log.debug("resulted user record - {}", oldUser);
        log.info("user with id - {} updated successfully", oldUser.getId());

        return oldUser;


    }

}
