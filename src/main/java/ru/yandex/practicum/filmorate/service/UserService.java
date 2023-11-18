package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        checkUserId(id);
        User user = userStorage.getUser(id);
        if (user == null) {
            throw new NotFoundException("Пользователь не найден");
        }
        return user;
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
    public User update(User user) {
        checkUserId(user.getId());
        validateService.userValidate(user);
        userStorage.update(user);
        return userStorage.getUser(user.getId());
    }

    @Override
    public List<User> getCommonFriends(Integer id, Integer otherId) {
        checkUserId(id);
        checkUserId(otherId);
        return userStorage.getCommonFriends(id, otherId);
    }

    @Override
    public List<User> getUserFriends(Integer id) {
        checkUserId(id);
        return userStorage.getFriends(id);
    }

    @Override
    public void updateFriendship(Integer id, Integer otherId, RequestMethod method) {
        checkUserId(id);
        checkUserId(otherId);
        if (method.equals(DELETE)) {
            userStorage.removeFriend(id, otherId);
        } else if (method.equals(PUT)) {
            userStorage.addFriend(id, otherId);
        } else {
            throw new ValidateException(String.format("Некорректный запрос действия " + method));
        }
    }

    private void checkUserId(Integer id) {
        if (id == null) {
            throw new ValidateException("Не указан id пользователя");
        } else if (!getAllForCheck().containsKey(id)) {
            throw new NotFoundException(String.format("Пользователь с таким id=%d не существует", id));
        }
    }

    private Map<Integer, User> getAllForCheck() {
        return userStorage.getAll();
    }
}
