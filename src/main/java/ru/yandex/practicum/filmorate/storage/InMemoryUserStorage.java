package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private int userId = 0;
    private final Map<Integer, User> users = new HashMap<>();
    private final Map<Integer, Set<Integer>> userFriends = new HashMap<>();

    @Override
    public Map<Integer, User> getAll() {
        return users;
    }

    @Override
    public User getUser(Optional<Integer> id) {
        if (id.isEmpty()) {
            throw new ValidateException("Не указан id пользователя");
        }
        return users.get(id.get());
    }

    @Override
    public User create(User user) {
        user.setId(++userId);
        users.put(user.getId(), user);
        userFriends.put(user.getId(), new HashSet<>());
        return user;
    }

    @Override
    public void update(User user) {
        users.put(user.getId(), user);
    }

    @Override
    public List<User> getCommonFriends(Integer id, Integer otherId) {
        Set<Integer> commonFriendsId = userFriends.get(id).stream()
                .filter(userFriends.get(otherId)::contains)
                .collect(Collectors.toSet());
        return commonFriendsId.stream()
                .map(users::get)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getFriends(Integer id) {
        Set<Integer> friends = userFriends.get(id);
        return friends.stream()
                .map(users::get)
                .collect(Collectors.toList());
    }

    @Override
    public void addFriend(Integer id, Integer otherId) {
        Set<Integer> initiatorFriends = userFriends.get(id);
        initiatorFriends.add(otherId);
        userFriends.put(id, initiatorFriends);
        Set<Integer> friendFriends = userFriends.get(otherId);
        friendFriends.add(id);
        userFriends.put(otherId, friendFriends);
    }

    @Override
    public void removeFriend(Integer id, Integer otherId) {
        Set<Integer> initiatorFriends = userFriends.get(id);
        initiatorFriends.remove(otherId);
        userFriends.put(id, initiatorFriends);
        Set<Integer> friendFriends = userFriends.get(otherId);
        friendFriends.remove(id);
        userFriends.put(otherId, friendFriends);
    }
}
