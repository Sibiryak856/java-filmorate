package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exceptions.FilmValidException;
import ru.yandex.practicum.filmorate.exceptions.UserValidException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

public class UserControllerTest {

    private UserController userController;

    @BeforeEach
    public void setUp() {
        userController = new UserController();
    }

    @Test
    public void testUserEmail() {
        User user = new User(1, "user", "login", "name", LocalDate.of(2000,10,10));
        User user1 = new User(1, "", "login", "name", LocalDate.of(2000,10,10));

        Assertions.assertThrows(UserValidException.class, () -> userController.create(user));
        Assertions.assertThrows(UserValidException.class, () -> userController.create(user1));
    }

    @Test
    public void testUserLogin() {
        User user = new User(1, "u@y.ru", "", "name", LocalDate.of(2000,10,10));
        User user1 = new User(1, "u@y.ru", "log in", "name", LocalDate.of(2000,10,10));

        Assertions.assertThrows(UserValidException.class, () -> userController.create(user));
        Assertions.assertThrows(UserValidException.class, () -> userController.create(user1));
    }

    @Test
    public void testEmptyUserName() {
        User user = new User(1, "u@y.ru", "login", "", LocalDate.of(2000,10,10));
        user.setName(null);
        Assertions.assertEquals(user.getLogin(), userController.create(user).getName(), "Логин не присвоился имени");
    }

    @Test
    public void testUserBirthday() {
        User user = new User(1, "u@y.ru", "login", "name", LocalDate.now().plusDays(1));

        Assertions.assertThrows(UserValidException.class, () -> userController.create(user));
    }
}
