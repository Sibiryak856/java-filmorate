package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Map;

public interface UserStorage {

    Map<Integer, User> getAll();

    User getUser(Integer id);

    User create(User user);

    void update(User user);

    List<User> getCommonFriends(Integer id, Integer otherId);

    List<User> getFriends(Integer id);

    void addFriend(Integer id, Integer otherId);

    void removeFriend(Integer id, Integer otherId);

}
