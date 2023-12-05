package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.Optional;

@Component("userDb")
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<User> getAll() {
        return null;
    }

    @Override
    public Optional<User> getUser(Long id) {
        return Optional.empty();
    }

    @Override
    public User create(User user) {
        return null;
    }

    @Override
    public void update(User user) {

    }

    @Override
    public List<User> getCommonFriends(Long id, Long otherId) {
        return null;
    }

    @Override
    public List<User> getFriends(Long id) {
        return null;
    }

    @Override
    public void addFriend(Long id, Long otherId) {

    }

    @Override
    public void removeFriend(Long id, Long otherId) {

    }
}
