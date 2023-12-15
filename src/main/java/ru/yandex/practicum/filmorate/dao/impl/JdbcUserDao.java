package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcUserDao implements UserDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query(
                "SELECT * \n" +
                        "from USERS",
                this::mapRowToUsers);
    }

    @Override
    public Optional<User> getUser(Long id) {
        SqlParameterSource params = new MapSqlParameterSource().addValue("id", id);
        User user = jdbcTemplate.query(
                "SELECT * \n" +
                        "FROM USERS\n" +
                        "WHERE USER_ID = :id",
                params,
                rs -> {
                    User extractedUser = null;
                    if (rs.next()) {
                        extractedUser = User.builder()
                                .id(rs.getLong("USER_ID"))
                                .email(rs.getString("EMAIL"))
                                .login(rs.getString("LOGIN"))
                                .name(rs.getString("USER_NAME"))
                                .birthday(rs.getDate("BIRTHDAY").toLocalDate())
                                .build();
                    }
                    return extractedUser;
                });
        return Optional.ofNullable(user);
    }

    @Override
    public User create(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
                .withTableName("USERS")
                .usingColumns("EMAIL", "LOGIN", "USER_NAME", "BIRTHDAY")
                .usingGeneratedKeyColumns("USER_ID");
        long id = simpleJdbcInsert.executeAndReturnKey(toMap(user)).longValue();
        user.setId(id);
        return user;
    }

    @Override
    public void update(User user) {
        jdbcTemplate.update(
                "UPDATE USERS SET " +
                        "EMAIL = :EMAIL, " +
                        "LOGIN = :LOGIN, " +
                        "USER_NAME = :USER_NAME, " +
                        "BIRTHDAY = :BIRTHDAY\n" +
                        "WHERE USER_ID = :USER_ID",
               new MapSqlParameterSource(toMap(user)));
    }

    @Override
    public List<User> getCommonFriends(Long id, Long otherId) {
        String sql = "SELECT * \n" +
                "FROM USERS as u\n" +
                "WHERE u.USER_ID IN (SELECT FRIEND_ID\n" +
                "FROM USER_FRIENDS\n" +
                "WHERE USER_ID IN (:id, :otherId)\n" +
                "GROUP BY FRIEND_ID\n" +
                "HAVING COUNT (FRIEND_ID) > 1)";
        return jdbcTemplate.query(sql,
                new MapSqlParameterSource()
                        .addValue("id", id)
                        .addValue("otherId", otherId),
                this::mapRowToUsers);
    }

    @Override
    public List<User> getFriends(Long id) {
        String sql = "SELECT * \n" +
                "FROM USERS\n" +
                "WHERE USER_ID IN (SELECT FRIEND_ID\n" +
                "FROM USER_FRIENDS\n" +
                "WHERE USER_ID = :id)";
        return jdbcTemplate.query(sql,
                new MapSqlParameterSource()
                        .addValue("id", id),
                this::mapRowToUsers);
    }

    @Override
    public void addFriend(Long id, Long otherId) {
        String sqlQuery = "MERGE INTO USER_FRIENDS (USER_ID, FRIEND_ID) VALUES (:id, :otherId)";
        jdbcTemplate.update(sqlQuery,
                new MapSqlParameterSource()
                .addValue("id", id)
                .addValue("otherId", otherId));
    }

    @Override
    public void removeFriend(Long id, Long otherId) {
        String sqlQuery = "DELETE FROM USER_FRIENDS\n" +
                "WHERE USER_ID = :id AND FRIEND_ID = :otherId";
        jdbcTemplate.update(sqlQuery,
                new MapSqlParameterSource()
                        .addValue("id", id)
                        .addValue("otherId", otherId));
    }

    public Map<String, Object> toMap(User user) {
        Map<String, Object> values = new HashMap<>();
        values.put("USER_ID", user.getId());
        values.put("EMAIL", user.getEmail());
        values.put("LOGIN", user.getLogin());
        values.put("USER_NAME", user.getName());
        values.put("BIRTHDAY", user.getBirthday());
        return values;
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
