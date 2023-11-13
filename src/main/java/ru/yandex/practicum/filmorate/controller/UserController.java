package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.UserValidException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController implements Controller<User> {
    private int userId = 0;

    private final Map<Integer, User> users = new HashMap<>();

    @Override
    @GetMapping
    public List<User> getAll() {
        log.info("GET /users {}", users);
        return new ArrayList<>(users.values());
    }

    @Override
    @GetMapping(value = "/{id}")
    public User get(@PathVariable Integer id) throws RuntimeException {
        if (users.containsKey(id)) {
            log.info("Get /user {}", users.get(id));
            return users.get(id);
        } else {
            String error = "Пользователя с таким id не существует";
            log.error(error);
            throw new UserValidException(error);
        }
    }

    @Override
    @PostMapping
    public User create(@RequestBody User user) throws RuntimeException {
        userValidate(user);
        user.setId(++userId);
        if (user.getName() == null || user.getName().isBlank()) user.setName(user.getLogin());
        log.info("Post /users {}", user);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    @PutMapping
    public User update(@RequestBody User user) throws RuntimeException {
        userValidate(user);
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.info("Put /users {} updated: {}", user.getId(), user);
        } else {
            String error = "Пользователя с таким id не существует";
            log.error(error);
            throw new UserValidException(error);
        }
        return user;
    }

    private static void userValidate(User user) throws RuntimeException {
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            String errorMessage = "Введен некорректный адрес электронной почты";
            log.error(errorMessage);
            throw new UserValidException(errorMessage);
        } else if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            String errorMessage = "Логин введен некорректно";
            log.error(errorMessage);
            throw new UserValidException(errorMessage);
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            String errorMessage = "Дата рождения не может быть позже текущей даты";
            log.error(errorMessage);
            throw new UserValidException(errorMessage);
        } else return;
    }
}
