package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {

    List<User> getAll();

    Optional<User> getUser(Long id);

    User create(User user);

    void update(User user);

    List<User> getCommonFriends(Long id, Long otherId);

    List<User> getFriends(Long id);

    void addFriend(Long id, Long otherId);

    void removeFriend(Long id, Long otherId);

}
