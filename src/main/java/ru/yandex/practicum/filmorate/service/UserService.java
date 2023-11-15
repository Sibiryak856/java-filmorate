package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    public UserStorage userStorage;

    @Autowired
    public UserService(@Qualifier("memoryUserStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriend(Integer initiatorUserId, Integer requestedUserId) { // with params
        User initiator = userStorage.getUser(initiatorUserId);

        User requestedUser = userStorage.getUser(requestedUserId);

        Set<Integer> initiatorFriends = initiator.getFriends();
        initiatorFriends.add(requestedUser.getId());

        Set<Integer> requestedUserFriends = requestedUser.getFriends();
        requestedUserFriends.add(initiator.getId());
    }

    public void deleteFriend(Integer initiatorUserId, Integer requestedUserId) {
        User initiator = userStorage.getUser(initiatorUserId);

        User requestedUser = userStorage.getUser(requestedUserId);

        Set<Integer> initiatorFriends = initiator.getFriends();
        initiatorFriends.remove(requestedUser.getId());

        Set<Integer> requestedUserFriends = requestedUser.getFriends();
        requestedUserFriends.remove(initiator.getId());

    }

    public List<User> getCommonFriends (Integer initiatorUserId, Integer requestedUserId) {
        User initiator = userStorage.getUser(initiatorUserId);
        Set<Integer> initiatorFriends = initiator.getFriends();

        User requestedUser = userStorage.getUser(requestedUserId);
        Set<Integer> requestedUserFriends = requestedUser.getFriends();

        Set<Integer> commonFriendsId = initiatorFriends.stream()
                .filter(requestedUserFriends::contains)
                .collect(Collectors.toSet());
        List<User> commonFriends = commonFriendsId.stream()
                .map(id -> userStorage.getAll().get(id))
                .collect(Collectors.toList());
        return commonFriends;
    }
}
