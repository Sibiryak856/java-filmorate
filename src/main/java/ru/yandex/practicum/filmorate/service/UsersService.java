package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.Optional;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@Service
public class UsersService implements UserService {

    public UserStorage userStorage;
    private final ValidateService validateService;

    @Autowired
    public UsersService(UserStorage userStorage, ValidateService validateService) {
        this.userStorage = userStorage;
        this.validateService = validateService;
    }

    @Override
    public List<User> getAll() {
        return userStorage.getAll();
    }

    @Override
    public User getUser(Integer id) {
        Optional<User> user = userStorage.getUser(id);
        if (user.isEmpty()) {
            throw new NotFoundException(String.format("Пользователь id=%d не найден", id));
        }
        return user.get();
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
        if (userStorage.getUser(user.getId()).isEmpty()) {
            throw new NotFoundException(String.format("Пользователь id=%d не найден", user.getId()));
        }
        validateService.userValidate(user);
        userStorage.update(user);
        return userStorage.getUser(user.getId()).get();
    }

    @Override
    public List<User> getCommonFriends(Integer id, Integer otherId) {
        if (userStorage.getUser(id).isEmpty()) {
            throw new NotFoundException(String.format("Пользователь id=%d не найден", id));
        } else if (userStorage.getUser(otherId).isEmpty()) {
            throw new NotFoundException(String.format("Пользователь id=%d не найден", otherId));
        }
        return userStorage.getCommonFriends(id, otherId);
    }

    @Override
    public List<User> getUserFriends(Integer id) {
        if (userStorage.getUser(id).isEmpty()) {
            throw new NotFoundException(String.format("Пользователь id=%d не найден", id));
        }
        return userStorage.getFriends(id);
    }

    @Override
    public void updateFriendship(Integer id, Integer otherId, RequestMethod method) {
        if (userStorage.getUser(id).isEmpty()) {
            throw new NotFoundException(String.format("Пользователь id=%d не найден", id));
        } else if (userStorage.getUser(otherId).isEmpty()) {
            throw new NotFoundException(String.format("Пользователь id=%d не найден", otherId));
        }

        if (method == DELETE) {
            userStorage.removeFriend(id, otherId);
        } else if (method == PUT) {
            userStorage.addFriend(id, otherId);
        }
    }
}
