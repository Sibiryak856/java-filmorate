package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.UserFriend;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static ru.yandex.practicum.filmorate.model.FriendshipStatus.APPROVED;

@Component("userDb")
//@Repository("userDb")
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<User> getAll() {
        String sql = "select * from USERS";

        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs));
    }

    @Override
    public Optional<User> getUser(Long id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from USERS where USER_ID = ?", id);

        if (!userRows.next()) {
            return Optional.empty();
        }

        return Optional.of(User.builder()
                .id(userRows.getLong("USER_ID"))
                .email(userRows.getString("EMAIL"))
                .login(userRows.getString("LOGIN"))
                .name(userRows.getString("USER_NAME"))
                .birthday(userRows.getDate("BIRTHDAY").toLocalDate())
                .build());
    }

    @Override
    public User create(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("USERS")
                .usingGeneratedKeyColumns("USER_ID");
        user.setId(simpleJdbcInsert.executeAndReturnKey(user.toMap()).longValue());
        return user;
       /*
        String sqlQuery = "insert into USERS(EMAIL, LOGIN, USER_NAME, BIRTHDAY) " +
                "values (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"USER_ID"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);
        return keyHolder.getKey().longValue();*/
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
        return null;
    }

    @Override
    public List<User> getFriends(Long id) {
        String sql = "select USER_ID, EMAIL, LOGIN, USER_NAME, BIRTHDAY from USERS u " +
                "join USER_FRIENDS uf on u.USER_ID = uf.USER_ID " +
                "group by uf.USER_ID " +
                "where uf.USER_ID = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs), id);
    }

    @Override
    public void addFriend(Long id, Long otherId) {


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
                    APPROVED.getValue());
    }

    @Override
    public void removeFriend(Long id, Long otherId) {

    }

    private User makeUser(ResultSet rs) throws SQLException {
        return User.builder()
                .id(rs.getLong("USER_ID"))
                .email(rs.getString("EMAIL"))
                .login(rs.getString("LOGIN"))
                .name(rs.getString("USER_NAME"))
                .birthday(rs.getDate("BIRTHDAY").toLocalDate())
                .build();
    }
}
