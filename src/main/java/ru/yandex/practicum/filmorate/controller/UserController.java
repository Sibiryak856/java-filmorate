package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidateException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {

    private UserStorage userStorage;

    @Autowired
    public UserController(@Qualifier("memoryUserStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @GetMapping
    public List<User> getAll() {
        return userStorage.getAll();
    }

    @GetMapping(value = "/{id}")
    public User getUser(@PathVariable Integer id) {
        return userStorage.getUser(id);
    }

    @PostMapping
    public User create(@RequestBody User user) {
        return userStorage.create(user);
    }

    @PutMapping
    public User update(@RequestBody User user) {
        return userStorage.update(user);
    }
}
