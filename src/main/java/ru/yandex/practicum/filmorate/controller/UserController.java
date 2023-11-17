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
        log.info("Request received: GET /users");
        List<User> users = userService.getAll();
        log.info("Request GET /users processed: {}", users);
        return users;
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Integer id) {
        log.info("Request received: GET /users/id={}", id);
        User user = userService.getUser(id);
        log.info("Request GET /users/id processed: {}", user);
        return user;
    }

    @GetMapping("/{id}/friends")
    public List<User> getUserFriends(@PathVariable Integer id) {
        log.info("Request received: GET /users/id={}/friends", id);
        User user = userService.getUser(id);
        List<User> userFriends = userService.getUserFriends(user);
        log.info("Request GET /users/id=/friends processed: {}", userFriends);
        return userFriends;
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(
            @PathVariable Integer id,
            @PathVariable Integer otherId
    ) {
        log.info("Request received: GET /users/id={}/friends/common/otherId={}", id, otherId);
        User user = userService.getUser(id);
        User friend = userService.getUser(otherId);
        List<User> commonFriends = userService.getCommonFriends(user, friend);
        log.info("Request GET /users/id/friends/common/otherId processed: {}", commonFriends);
        return commonFriends;
    }

    @PostMapping
    public User create(@RequestBody User user) {
        log.info("Request received: POST /users: {}", user);
        User createdUser = userService.create(user);
        log.info("Request POST /users processed: user={} is created", createdUser);
        return createdUser;
    }

    @PutMapping
    public User update(@RequestBody User user) {
        log.info("Request received: PUT /users: {}", user);
        userService.update(user);
        log.info("Request PUT /users processed: user={} is updated", user.getId());
        return userService.getUser(user.getId());
    }


    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(
            @PathVariable Integer id,
            @PathVariable Integer friendId
    ) {
        log.info("Request received: PUT /users/id={}/friends/friendId={}", id, friendId);
        userService.updateFriendship(id, friendId, RequestMethod.PUT);
        log.info("Request PUT /users/id/friends/friendId processed");
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFriend(
            @PathVariable Integer id,
            @PathVariable Integer friendId
    ) {
        log.info("Request received: DELETE /users/id={}/friends/friendId={}", id, friendId);
        userService.updateFriendship(id, friendId, RequestMethod.DELETE);
        log.info("Request DELETE /users/id/friends/friendId processed");
    }
}
