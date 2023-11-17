package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@Service
public class UserService implements UserSvc {

    public UserStorage userStorage;
    private final ValidateService validateService;

    @Autowired
    public UserService(UserStorage userStorage, ValidateService validateService) {
        this.userStorage = userStorage;
        this.validateService = validateService;
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(userStorage.getAll().values());
    }

    @Override
    public User getUser(Integer id) {
        validateService.userIdValidate(userStorage.getAll(), id);
        return userStorage.getUser(id);
    }

    @Override
    public User create(User user) {
        validateService.userValidate(user);
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return userStorage.create(user);
    }

    @Override
    public void update(User user) {
        validateService.userIdValidate(userStorage.getAll(), user.getId());
        validateService.userValidate(user);
        userStorage.update(user);
    }

    @Override
    public List<User> getCommonFriends(User user, User friend) {
        return userStorage.getCommonFriends(user, friend);
    }

    @Override
    public List<User> getUserFriends(User user) {
        return userStorage.getFriends(user);
    }

    @Override
    public void updateFriendship(Integer userId, Integer friendId, RequestMethod method) {
        User user = getUser(userId);
        User friend = getUser(friendId);
        if (method.equals(DELETE)) {
            userStorage.removeFriend(user, friend);
        } else if (method.equals(PUT)) {
            userStorage.addFriend(user, friend);
        } else {
            throw new ValidateException(String.format("Некорректный запрос действия " + method));
        }
    }
}
