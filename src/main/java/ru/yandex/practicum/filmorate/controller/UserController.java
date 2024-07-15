package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private final static Logger log = LoggerFactory.getLogger(UserController.class);

    Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("trying to create new user record");
        log.debug("parsed user record - {}", user);
        try {
            if (user.getLogin().contains(" ")) {
                throw new ValidationException("Логин не может содержать пробелов");
            }
            if (user.getBirthday().isAfter(LocalDate.now())) {
                throw new ValidationException("День рождения не может быть в будущем");
            }
            if (user.getName() == null || user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
        } catch (Exception e) {
            log.error("error while trying to validate request body: ", e);
            throw new RuntimeException(e);
        }
        log.trace("calculating new id");
        user.setId(getNextId());
        users.put(user.getId(), user);

        return user;

    }

    @PutMapping
    public User update(@Valid @RequestBody User newUser) {
        log.info("trying to update user record");
        try {
            if (newUser.getId() == null) {
                throw new ConditionsNotMetException("id должен быть указан");
            }
            log.debug("updating user with id - {}", newUser.getId());
            if (users.containsKey(newUser.getId())) {
                User oldUser = users.get(newUser.getId());
                log.debug("old user record - {}", oldUser);
                log.debug("new user record - {}", newUser);
                for (User u : users.values()) {
                    if (newUser.getEmail().equals(u.getEmail())) {
                        throw new ConditionsNotMetException("Этот email уже используется");
                    }
                }
                oldUser.setEmail(newUser.getEmail());
                oldUser.setLogin(newUser.getLogin());
                if (newUser.getName() != null && !newUser.getName().isBlank()) {
                    oldUser.setName(newUser.getName());
                } else {
                    oldUser.setName(oldUser.getLogin());
                }
                if (newUser.getBirthday().isAfter(LocalDate.now())) {
                    throw new ValidationException("День рождения не может быть в будущем");
                }
                oldUser.setBirthday(newUser.getBirthday());
                log.debug("resulted user record - {}", oldUser);
                log.info("user with id - {} updated successfully", oldUser.getId());
                return oldUser;
            }
            throw new NotFoundException("Пользователь с id = " + newUser.getId() + " не найден");
        } catch (Exception e) {
            log.error("error while trying to update user record: ", e);
            throw new RuntimeException(e);
        }

    }

    private Long getNextId() {
        long currentLastId = users.keySet().stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        log.trace("{} is next available id", currentLastId + 1);
        return ++currentLastId;
    }
}
