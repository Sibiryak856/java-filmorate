package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository("userDbStorage")
@RequiredArgsConstructor
public class UserDbDaoImpl implements UserDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query(
                "SELECT * \n" +
                        "from USERS",
                this::mapRowToUsers);
    }

    @Override
    public Optional<User> getUser(Long id) {
        User user;
        try {
            user = jdbcTemplate.queryForObject(
                    "SELECT * \n" +
                            "FROM USERS\n" +
                            "WHERE USER_ID = ?",
                    this::mapRowToUsers,
                    id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException(String.format("User id=%d not found", id));
        }
        return Optional.ofNullable(user);
    }

    @Override
    public User create(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("USERS")
                .usingGeneratedKeyColumns("USER_ID");
        long id = simpleJdbcInsert.executeAndReturnKey(user.toMap()).longValue();
        user.setId(id);
        return user;
    }

    @Override
    public void update(User user) {
        jdbcTemplate.update(
                "UPDATE USERS\n" +
                        "SET EMAIL = ?, LOGIN = ?, USER_NAME = ?, BIRTHDAY = ?\n" +
                        "WHERE USER_ID = ?",
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());
    }

    @Override
    public List<User> getCommonFriends(Long id, Long otherId) {
        String sql = "SELECT * \n" +
                "FROM USERS as u\n" +
                "WHERE u.USER_ID IN (SELECT FRIEND_ID\n" +
                "FROM USER_FRIENDS\n" +
                "WHERE USER_ID IN (?, ?)\n" +
                "GROUP BY FRIEND_ID\n" +
                "HAVING COUNT (FRIEND_ID) > 1)";
        return jdbcTemplate.query(sql, this::mapRowToUsers, id, otherId);
    }

    @Override
    public List<User> getFriends(Long id) {
        String sql = "SELECT * \n" +
                "FROM USERS\n" +
                "WHERE USER_ID IN (SELECT FRIEND_ID\n" +
                "FROM USER_FRIENDS\n" +
                "WHERE USER_ID = ?)";
        return jdbcTemplate.query(sql, this::mapRowToUsers, id);
    }

    @Override
    public void addFriend(Long id, Long otherId) {
        String sqlQuery = "MERGE INTO USER_FRIENDS (USER_ID, FRIEND_ID) VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery, id, otherId);
    }

    @Override
    public void removeFriend(Long id, Long otherId) {
        String sqlQuery = "DELETE FROM USER_FRIENDS\n" +
                "WHERE USER_ID = ? AND FRIEND_ID = ?";
        jdbcTemplate.update(sqlQuery, id, otherId);
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
