package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.Constants.FRIEDNS_LIST_ACTIONS;
import static ru.yandex.practicum.filmorate.Constants.FRIEND_CANCELLATION;

@Service
public class UserService {

    public UserStorage userStorage;

    @Autowired
    public UserService(@Qualifier("memoryUserStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

/*    public void addFriend(Integer initiatorUserId, Integer requestedUserId) { // with params
        User initiator = userStorage.getUser(initiatorUserId);

        User requestedUser = userStorage.getUser(requestedUserId);

        Set<Integer> initiatorFriends = initiator.getFriends();
        initiatorFriends.add(requestedUser.getId());
        initiator.setFriends(initiatorFriends);

        Set<Integer> requestedUserFriends = requestedUser.getFriends();
        requestedUserFriends.add(initiator.getId());
        requestedUser.setFriends(requestedUserFriends);
    }

    public void deleteFriend(Integer initiatorUserId, Integer requestedUserId) {
        User initiator = userStorage.getUser(initiatorUserId);

        User requestedUser = userStorage.getUser(requestedUserId);

        Set<Integer> initiatorFriends = initiator.getFriends();
        initiatorFriends.remove(requestedUser.getId());
        initiator.setFriends(initiatorFriends);

        Set<Integer> requestedUserFriends = requestedUser.getFriends();
        requestedUserFriends.remove(initiator.getId());
        requestedUser.setFriends(requestedUserFriends);

    }*/

    public List<User> getCommonFriends (Integer initiatorUserId, Integer requestedUserId) {
        User initiator = userStorage.getUser(initiatorUserId);
        Set<Integer> initiatorFriends = initiator.getFriends();

        User requestedUser = userStorage.getUser(requestedUserId);
        Set<Integer> requestedUserFriends = requestedUser.getFriends();

        Set<Integer> commonFriendsId = initiatorFriends.stream()
                .filter(requestedUserFriends::contains)
                .collect(Collectors.toSet());
        return commonFriendsId.stream()
                .map(id -> userStorage.getAll().get(id))
                .collect(Collectors.toList());
    }

    public User updateFriend(Integer initiatorId, Integer requestedId, String action) {
        User initiator = userStorage.getUser(initiatorId);
        User requestedUser = userStorage.getUser(requestedId);

        if (!FRIEDNS_LIST_ACTIONS.contains(action)) {
            throw new IncorrectParameterException(action);
        }
        Set<Integer> initiatorFriends = initiator.getFriends();
        Set<Integer> requestedUserFriends = requestedUser.getFriends();
        if(action.equals(FRIEND_CANCELLATION)) {
            initiatorFriends.remove(requestedId);
            requestedUserFriends.remove(initiatorId);
            initiator.setFriends(initiatorFriends);
            requestedUser.setFriends(requestedUserFriends);
            return initiator;
        }
        initiatorFriends.add(requestedId);
        requestedUserFriends.add(initiatorId);
        initiator.setFriends(initiatorFriends);
        requestedUser.setFriends(requestedUserFriends);
        return initiator;
    }
}
