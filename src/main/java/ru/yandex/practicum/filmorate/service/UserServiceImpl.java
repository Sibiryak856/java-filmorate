package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@Service
public class UserServiceImpl implements UserService {

    public UserStorage userStorage;
    private final ValidateService validateService;

    @Autowired
    public UserServiceImpl(UserStorage userStorage, ValidateService validateService) {
        this.userStorage = userStorage;
        this.validateService = validateService;
    }

    @Override
    public List<User> getAll() {
        return userStorage.getAll();
    }

    @Override
    public User getUser(Long id) {
        return userStorage.getUser(id)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь id=%d не найден", id)));
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
        User updatingUser = userStorage.getUser(user.getId())
                .orElseThrow(() -> new NotFoundException("Обновляемый пользователь не найден"));
        validateService.userValidate(user);
        userStorage.update(user);
        return userStorage.getUser(user.getId()).get();
    }

    @Override
    public List<User> getCommonFriends(Long id, Long otherId) {
        User user = userStorage.getUser(id)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь id=%d не найден", id)));
        User otherUser = userStorage.getUser(otherId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь id=%d не найден", otherId)));

        return userStorage.getCommonFriends(id, otherId);
    }

    @Override
    public List<User> getUserFriends(Long id) {
        User user = userStorage.getUser(id)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь id=%d не найден", id)));
        return userStorage.getFriends(id);
    }

    @Override
    public void updateFriendship(Long id, Long otherId, RequestMethod method) {
        User user = userStorage.getUser(id)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь id=%d не найден", id)));
        User otherUser = userStorage.getUser(otherId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь id=%d не найден", otherId)));

        if (method == DELETE) {
            userStorage.removeFriend(id, otherId);
        } else if (method == PUT) {
            userStorage.addFriend(id, otherId);
        }
    }
}
