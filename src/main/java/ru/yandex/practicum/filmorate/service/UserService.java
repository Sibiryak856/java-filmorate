package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@Service
@Slf4j
public class UserService {

    public UserStorage userStorage;

    @Autowired
    public UserService(@Qualifier("memoryUserStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> getCommonFriends (Integer userId, Integer friendId) {
        User initiator = userStorage.getUser(userId);
        Set<Integer> initiatorFriends = initiator.getFriends();

        User requestedUser = userStorage.getUser(friendId);
        Set<Integer> requestedUserFriends = requestedUser.getFriends();

        Set<Integer> commonFriendsId = initiatorFriends.stream()
                .filter(requestedUserFriends::contains)
                .collect(Collectors.toSet());
        log.info("Обработан запрос вывода общих друзей ids {} и {}", userId, friendId);
        return commonFriendsId.stream()
                .map(id -> userStorage.getAll().get(id))
                .collect(Collectors.toList());
    }

    public List<User> getFriends (Integer userId) {
        User initiator = userStorage.getUser(userId);
        Set<Integer> friends = initiator.getFriends();

        log.info("Обработан запрос вывода друзей пользователя {} ", userId);
        return friends.stream()
                .map(id -> userStorage.getAll().get(id))
                .collect(Collectors.toList());
    }

    public void updateFriendship(Integer userId, Integer friendId, RequestMethod method) {
        User initiator = userStorage.getUser(userId);
        User requestedUser = userStorage.getUser(friendId);

        Set<Integer> initiatorFriends = initiator.getFriends();
        Set<Integer> requestedUserFriends = requestedUser.getFriends();
        if(method.equals(DELETE)) {
            initiatorFriends.remove(friendId);
            requestedUserFriends.remove(userId);
            initiator.setFriends(initiatorFriends);
            requestedUser.setFriends(requestedUserFriends);
            log.info("Пользователь id {} успешно удалил пользователя id {} из списка друзей", userId, friendId);
        } else if (method.equals(PUT)) {
            initiatorFriends.add(friendId);
            requestedUserFriends.add(userId);
            initiator.setFriends(initiatorFriends);
            requestedUser.setFriends(requestedUserFriends);
            log.info("Пользователь id {} успешно добавил пользователя id {} в список друзей", userId, friendId);
        } else {
            String msg = "Некорректный запрос действия " + method;
            log.error(msg);
            throw new ValidateException(msg);
        }
    }
}
