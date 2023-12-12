package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository("userDbStorage")
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query(
                "select * from USERS",
                this::mapRowToUsers);
    }

    @Override
    public Optional<User> getUser(Long id) {
        return Optional.ofNullable(
                jdbcTemplate.queryForObject(
                "select * from USERS where USER_ID = ?",
                this::mapRowToUsers,
                id));
    }

    @Override
    public User create(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("USERS")
                .usingGeneratedKeyColumns("USER_ID");
        user.setId(simpleJdbcInsert.executeAndReturnKey(user.toMap()).longValue());
        return user;
    }

    @Override
    public void update(User user) {
        String sqlQuery = "update USERS set " +
                "EMAIL = ?, LOGIN = ?, NAME = ?, BIRTHDAY = ? " +
                "where USER_ID = ?";
        jdbcTemplate.update(sqlQuery,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());
    }

    @Override
    public List<User> getCommonFriends(Long id, Long otherId) {
        String sql = "select * from USERS as u " +
                "where u.USER_ID in " +
                "(select FRIEND_ID " +
                "from USER_FRIENDS " +
                "where USER_ID in (?, ?) " +
                "group by FRIEND_ID " +
                "having count(FRIENDS_ID) > 1)";
        return jdbcTemplate.query(sql, this::mapRowToUsers, id, otherId);
    }

    @Override
    public List<User> getFriends(Long id) {
        String sql = "SELECT * FROM USERS WHERE USER_ID IN " +
                "(SELECT FRIEND_ID FROM USER_FRIENDS WHERE USER_ID = ?)";
        return jdbcTemplate.query(sql, this::mapRowToUsers, id);
    }

    @Override
    public void addFriend(Long id, Long otherId) {
/*

        UserFriend userFriend = UserFriend.builder().
                userId(id).
                friendId(otherId).
                build();
        UserFriend addedUserFriend = UserFriend.builder().
                userId(otherId).
                friendId(id).
                build();
            String sqlQuery = "insert into USER_FRIENDS(USER_ID, FRIEND_ID, FRIEND_STATUS) " +
                    "values (?, ?, ?)";
            jdbcTemplate.update(sqlQuery,
                    id,
                    otherId,
                    APPROVED.getName());*/
    }

    @Override
    public void removeFriend(Long id, Long otherId) {

    }

    private User mapRowToUsers(ResultSet rs, int rowNum) throws SQLException {
        return User.builder()
                .id(rs.getLong("USER_ID"))
                .email(rs.getString("EMAIL"))
                .login(rs.getString("LOGIN"))
                .name(rs.getString("USER_NAME"))
                .birthday(rs.getDate("BIRTHDAY").toLocalDate())
                .build();
    }
}
