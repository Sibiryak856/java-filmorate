package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidateException;
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
        userStorage = new InMemoryUserStorage();
        userService = new UserService(userStorage, validateService);
        userController = new UserController(userService);
    }

    @Test
    public void testUserEmail() {
        User user = User.builder()
                .email("user")
                .login("login")
                .name("name")
                .birthday(LocalDate.of(2000,10,10))
                .build();
        User user1 = User.builder()
                .email("")
                .login("login")
                .name("name")
                .birthday(LocalDate.of(2000,10,10))
                .build();

        Assertions.assertThrows(ValidateException.class, () -> userController.create(user));
        Assertions.assertThrows(ValidateException.class, () -> userController.create(user1));
    }

    @Test
    public void testUserLogin() {
        User user = User.builder()
                .email("u@y.ru")
                .login("")
                .name("name")
                .birthday(LocalDate.of(2000,10,10))
                .build();
        User user1 = User.builder()
                .email("u@y.ru")
                .login("log in")
                .name("name")
                .birthday(LocalDate.of(2000,10,10))
                .build();

        Assertions.assertThrows(ValidateException.class, () -> userController.create(user));
        Assertions.assertThrows(ValidateException.class, () -> userController.create(user1));
    }

    @Test
    public void testEmptyUserName() {
        User user = User.builder()
                .email("u@y.ru")
                .login("login")
                .birthday(LocalDate.of(2000,10,10))
                .build();

        Assertions.assertEquals(user.getLogin(), userController.create(user).getName(), "Логин не присвоился имени");
    }

    @Test
    public void testUserBirthday() {
        User user = User.builder()
                .email("u@y.ru")
                .login("login")
                .name("name")
                .birthday(LocalDate.now().plusDays(1))
                .build();

        Assertions.assertThrows(ValidateException.class, () -> userController.create(user));
    }
}