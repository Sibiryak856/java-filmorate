package ru.yandex.practicum.filmorate.service;

import org.springframework.web.bind.annotation.RequestMethod;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserSvc {
    List<User> getAll();

    User getUser(Integer id);

    User create(User user);

    void update(User user);

    List<User> getCommonFriends(User user, User friend);

    List<User> getUserFriends(User user);

    void updateFriendship(Integer userId, Integer friendId, RequestMethod method);

}
