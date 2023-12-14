package ru.yandex.practicum.filmorate.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.service.ValidateService;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@Service
public class UserServiceImpl implements UserService {

    public UserDao userDao;
    private final ValidateService validateService;

    @Autowired
    public UserServiceImpl(@Qualifier("userDbStorage") UserDao userDao,
                           ValidateService validateService) {
        this.userDao = userDao;
        this.validateService = validateService;
    }

    @Override
    public List<User> getAll() {
        return userDao.getAll();
    }

    @Override
    public User getUser(Long id) {
        return userDao.getUser(id)
                .orElseThrow(() -> new NotFoundException(String.format("User id=%d not found", id)));
    }

    @Override
    public User create(User user) {
        validateService.userValidate(user);
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return userDao.create(user);
    }

    @Override
    public User update(User user) {
        User updatingUser = userDao.getUser(user.getId())
                .orElseThrow(() -> new NotFoundException("Updating user not found"));
        validateService.userValidate(user);
        userDao.update(user);
        return userDao.getUser(user.getId()).get();
    }

    @Override
    public List<User> getCommonFriends(Long id, Long otherId) {
        User user = userDao.getUser(id)
                .orElseThrow(() -> new NotFoundException(String.format("User id=%d not found", id)));
        User otherUser = userDao.getUser(otherId)
                .orElseThrow(() -> new NotFoundException(String.format("User id=%d not found", otherId)));

        return userDao.getCommonFriends(id, otherId);
    }

    @Override
    public List<User> getUserFriends(Long id) {
        User user = userDao.getUser(id)
                .orElseThrow(() -> new NotFoundException(String.format("User id=%d not found", id)));
        return userDao.getFriends(id);
    }

    @Override
    public void updateFriendship(Long id, Long otherId, RequestMethod method) {
        User user = userDao.getUser(id)
                .orElseThrow(() -> new NotFoundException(String.format("User id=%d not found", id)));
        User otherUser = userDao.getUser(otherId)
                .orElseThrow(() -> new NotFoundException(String.format("User id=%d not found", otherId)));

        if (method == DELETE) {
            userDao.removeFriend(id, otherId);
        } else if (method == PUT) {
            userDao.addFriend(id, otherId);
        }
    }
}
