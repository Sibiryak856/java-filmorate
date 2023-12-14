package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.dao.impl.UserDbDaoImpl;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@JdbcTest
public class TestUserDbStorage {

    private final JdbcTemplate jdbcTemplate;

    private UserDbDaoImpl userDao;
    private User user;
    private User friend;

    @BeforeEach
    public void setUp() {
        userDao = new UserDbDaoImpl(jdbcTemplate);
        user =  User.builder()
                .id(1L)
                .email("u@y.ru")
                .login("login")
                .name("name")
                .birthday(LocalDate.of(2000,10,10))
                .build();
        friend =  User.builder()
                .id(2L)
                .email("second@y.ru")
                .login("login2")
                .name("name2")
                .birthday(LocalDate.of(2010,10,10))
                .build();
    }

    @Test
    public void testGetAllUsers() {
        userDao.create(user);

        List<User> users = userDao.getAll();

        Assertions.assertThat(users)
                .isNotNull()
                .isNotEmpty()
                .usingRecursiveComparison()
                .isEqualTo(Arrays.asList(user));
    }

    @Test
    public void testGetUser() {
        userDao.create(user);

        Optional<User> res = userDao.getUser(user.getId());

        Assertions.assertThat(res.get())
                .usingRecursiveComparison()
                .isEqualTo(user);
    }

    @Test
    public void testUpdateUser() {
        userDao.create(user);
        User userToUpdate = user;
        userToUpdate.setName("newName");
        userDao.update(userToUpdate);
        user.setName("newName");

        Optional<User> res = userDao.getUser(userToUpdate.getId());

        Assertions.assertThat(res.get())
                .usingRecursiveComparison()
                .isEqualTo(user);
    }

    @Test
    public void testGetCommonFriends() {
        userDao.create(user);
        userDao.create(friend);
        User thirdUser = user;
        thirdUser.setId(3L);
        thirdUser.setName("thirdUser");
        userDao.create(thirdUser);
        userDao.addFriend(user.getId(), thirdUser.getId());
        userDao.addFriend(friend.getId(), thirdUser.getId());

        List<User> commonFriends = userDao.getCommonFriends(user.getId(), friend.getId());

        Assertions.assertThat(commonFriends)
                .isNotNull()
                .isNotEmpty();

        Assertions.assertThat(commonFriends.get(0))
                .usingRecursiveComparison()
                .isEqualTo(thirdUser);
    }

    @Test
    public void testAddAndGetFriends() {
        userDao.create(user);
        userDao.create(friend);
        userDao.addFriend(user.getId(), friend.getId());

        List<User> userFriends = userDao.getFriends(user.getId());

        Assertions.assertThat(userFriends)
                .isNotNull()
                .isNotEmpty();

        Assertions.assertThat(userFriends.get(0))
                .usingRecursiveComparison()
                .isEqualTo(friend);
    }

    @Test
    public void testRemoveFriend() {
        userDao.create(user);
        userDao.create(friend);
        userDao.addFriend(user.getId(), friend.getId());

        List<User> userFriends = userDao.getFriends(user.getId());

        Assertions.assertThat(userFriends)
                .isNotNull()
                .isNotEmpty();

        Assertions.assertThat(userFriends.get(0))
                .usingRecursiveComparison()
                .isEqualTo(friend);

        userDao.removeFriend(user.getId(), friend.getId());

        List<User> newUserFriends = userDao.getFriends(user.getId());

        Assertions.assertThat(newUserFriends)
                .isNotNull()
                .isEmpty();
    }
}
