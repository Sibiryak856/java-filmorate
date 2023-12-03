package ru.yandex.practicum.filmorate.service;

import org.springframework.web.bind.annotation.RequestMethod;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {
    List<User> getAll();

    User getUser(Long id);

    User create(User user);

    User update(User user);

    List<User> getCommonFriends(Long id, Long otherId);

    List<User> getUserFriends(Long id);

    void updateFriendship(Long id, Long otherId, RequestMethod method);

}
