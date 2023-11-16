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

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping
    public List<User> getAll() {
        return userService.userStorage.getAll();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Integer id) {
        return userService.userStorage.getUser(id);
    }

    @GetMapping("/{id}/friends")
    public List<User> getUserFriends(@PathVariable Integer id) {
        return userService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}") // список общих друзей id и otherId
    public List<User> getCommonFriends(
            @PathVariable Integer id,
            @PathVariable Integer otherId
    ) {
        return userService.getCommonFriends(id, otherId);
    }

    @PostMapping
    public User create(@RequestBody User user) {
        return userService.userStorage.create(user);
    }

    @PutMapping
    public User update(@RequestBody User user) {
        return userService.userStorage.update(user);
    }


    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(
            @PathVariable Integer id,
            @PathVariable Integer friendId
    ) {
        userService.updateFriendship(id, friendId, RequestMethod.PUT);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(
            @PathVariable Integer id,
            @PathVariable Integer friendId
    ) {
        userService.updateFriendship(id, friendId, RequestMethod.DELETE);
    }
}
