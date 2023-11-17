package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
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
    public User getUser(Integer id) {
        return users.get(id);
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
    public List<User> getCommonFriends(User user, User friend) {
        Set<Integer> commonFriendsId = userFriends.get(user.getId()).stream()
                .filter(userFriends.get(friend.getId())::contains)
                .collect(Collectors.toSet());
        return commonFriendsId.stream()
                .map(users::get)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getFriends(User user) {
        Set<Integer> friends = userFriends.get(user.getId());
        return friends.stream()
                .map(users::get)
                .collect(Collectors.toList());
    }

    @Override
    public void addFriend(User initiator, User friend) {
        Set<Integer> initiatorFriends = userFriends.get(initiator.getId());
        initiatorFriends.add(friend.getId());
        userFriends.put(initiator.getId(), initiatorFriends);
        Set<Integer> friendFriends = userFriends.get(friend.getId());
        friendFriends.add(initiator.getId());
        userFriends.put(friend.getId(), friendFriends);
    }

    @Override
    public void removeFriend(User initiator, User friend) {
        Set<Integer> initiatorFriends = userFriends.get(initiator.getId());
        initiatorFriends.remove(friend.getId());
        userFriends.put(initiator.getId(), initiatorFriends);
        Set<Integer> friendFriends = userFriends.get(friend.getId());
        friendFriends.remove(initiator.getId());
        userFriends.put(friend.getId(), friendFriends);
    }
}
