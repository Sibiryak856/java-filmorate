package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private Long userId = 0L;
    private final Map<Long, User> users = new HashMap<>();
    private final Map<Long, Set<Long>> userFriends = new HashMap<>();
    //private final Map<Integer, Friendship> friendsMutuality = new HashMap<>();

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public Optional<User> getUser(Long id) {
        return Optional.ofNullable(users.get(id));
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
    public List<User> getCommonFriends(Long id, Long otherId) {
        Set<Long> commonFriendsId = userFriends.get(id).stream()
                .filter(userFriends.get(otherId)::contains)
                .collect(Collectors.toSet());
        return commonFriendsId.stream()
                .map(users::get)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getFriends(Long id) {
        Set<Long> friends = userFriends.get(id);
        return friends.stream()
                .map(users::get)
                .collect(Collectors.toList());
    }

    @Override
    public void addFriend(Long id, Long otherId) {
        Set<Long> initiatorFriends = userFriends.get(id);
        initiatorFriends.add(otherId);
        userFriends.put(id, initiatorFriends);
        Set<Long> friendFriends = userFriends.get(otherId);
        friendFriends.add(id);
        userFriends.put(otherId, friendFriends);
    }

    @Override
    public void removeFriend(Long id, Long otherId) {
        Set<Long> initiatorFriends = userFriends.get(id);
        initiatorFriends.remove(otherId);
        userFriends.put(id, initiatorFriends);
        Set<Long> friendFriends = userFriends.get(otherId);
        friendFriends.remove(id);
        userFriends.put(otherId, friendFriends);
    }
}
