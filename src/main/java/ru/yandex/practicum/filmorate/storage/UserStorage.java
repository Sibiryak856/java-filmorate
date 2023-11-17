package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Map;

public interface UserStorage {

    Map<Integer, User> getAll();

    User getUser(Integer id);

    User create(User user);

    void update(User user);

    List<User> getCommonFriends(User user, User friend);

    List<User> getFriends(User user);

    void addFriend(User user, User friend);

    void removeFriend(User user, User friend);

}
