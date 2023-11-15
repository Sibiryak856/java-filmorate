/*
package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exceptions.ValidateException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.service.ValidateService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;


public class UserControllerTest {

    private ValidateService validateService;
    private UserService userService;
    private InMemoryUserStorage userStorage;
    private UserController userController;

    @BeforeEach
    public void setUp() {
        validateService = new ValidateService();
        userService = new UserService();
        userStorage = new InMemoryUserStorage(validateService, userService);
        userController = new UserController(userStorage);
    }

    @Test
    public void testUserEmail() {
        User user = new User(1, "user", "login", "name", LocalDate.of(2000,10,10));
        User user1 = new User(1, "", "login", "name", LocalDate.of(2000,10,10));

        Assertions.assertThrows(ValidateException.class, () -> userController.create(user));
        Assertions.assertThrows(ValidateException.class, () -> userController.create(user1));
    }

    @Test
    public void testUserLogin() {
        User user = new User(1, "u@y.ru", "", "name", LocalDate.of(2000,10,10));
        User user1 = new User(1, "u@y.ru", "log in", "name", LocalDate.of(2000,10,10));

        Assertions.assertThrows(ValidateException.class, () -> userController.create(user));
        Assertions.assertThrows(ValidateException.class, () -> userController.create(user1));
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

        Assertions.assertThrows(ValidateException.class, () -> userController.create(user));
    }
}
*/
