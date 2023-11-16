package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping
    public List<User> getAll() {
        return userService.userStorage.getAll();
    }

    @GetMapping(value = "/{id}")
    public User getUser(@PathVariable Integer id) {
        return userService.userStorage.getUser(id);
    }

    @GetMapping("/common")
    public List<User> getCommonFriends(
            @RequestParam() Integer initiatorId,
            @RequestParam() Integer requestedId
    ) {
        return userService.getCommonFriends(initiatorId, requestedId);
    }

    @PostMapping
    public User create(@RequestBody User user) {
        return userService.userStorage.create(user);
    }

    @PutMapping
    public User update(@RequestBody User user) {
        return userService.userStorage.update(user);
    }

    @PutMapping("/friend")
    public User updateFriends(
            @RequestParam() Integer initiatorId,
            @RequestParam() Integer requestedId,
            @RequestParam() String action
    ) {
        return userService.updateFriend(initiatorId, requestedId, action);
    }
}
