package ru.yandex.practicum.filmorate.service;

import org.springframework.web.bind.annotation.RequestMethod;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {
    List<User> getAll();

    User getUser(Integer id);

    User create(User user);

    User update(User user);

    List<User> getCommonFriends(Integer id, Integer otherId);

    List<User> getUserFriends(Integer id);

    void updateFriendship(Integer id, Integer otherId, RequestMethod method);

}
