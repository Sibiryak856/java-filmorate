package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidateException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.ValidateService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {
    private int userId = 0;

    private final Map<Integer, User> users = new HashMap<>();
    ValidateService validateService = new ValidateService();

    @GetMapping
    public List<User> getAll() {
        log.info("GET /users {}", users);
        return new ArrayList<>(users.values());
    }

    @GetMapping(value = "/{id}")
    public User get(@PathVariable Integer id) {
        if (!users.containsKey(id)) {
            String error = "Пользователя с таким id не существует";
            log.error(error);
            throw new ValidateException(error);
        }
        log.info("Get /user {}", users.get(id));
        return users.get(id);
    }

    @PostMapping
    public User create(@RequestBody User user) {
        validateService.userValidate(user);
        user.setId(++userId);
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        log.info("Post /users {}", user);
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User update(@RequestBody User user) {
        if (user.getId() == null) {
            String error = "Не указан id пользовавтеля";
            log.error(error);
            throw new ValidateException(error);
        }

        validateService.userValidate(user);
        if (!users.containsKey(user.getId())) {
            String error = "Пользователя с таким id не существует";
            log.error(error);
            throw new ValidateException(error);
        }
        users.put(user.getId(), user);
        log.info("Put /users {} updated: {}", user.getId(), user);
        return user;
    }
}
