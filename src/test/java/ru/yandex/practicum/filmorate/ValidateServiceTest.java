package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.ValidateService;

import java.time.LocalDate;

public class ValidateServiceTest {

    private ValidateService validateService;

    @BeforeEach
    public void setUp() {
        validateService = new ValidateService();
    }

    @Test
    public void testEmptyFilmName() {
        Film film = Film.builder()
                .name("")
                .description("description")
                .releaseDate(LocalDate.of(2000, 10, 10))
                .duration(100)
                .build();

        Assertions.assertThrows(ValidateException.class, () -> validateService.filmValidate(film));
    }

    @Test
    public void testLengthFilmDescription() {
        Film film = Film.builder()
                .name("name")
                .description("descriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescription" +
                        "descriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescription" +
                        "descriptiondescriptiondescriptiondescriptiondescription")
                .releaseDate(LocalDate.of(2000, 10, 10))
                .duration(100)
                .build();

        Assertions.assertThrows(ValidateException.class, () -> validateService.filmValidate(film));
    }

    @Test
    public void testFilmReleaseDate() {
        Film film = Film.builder()
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(1895, 12, 27))
                .duration(100)
                .build();

        Assertions.assertThrows(ValidateException.class, () -> validateService.filmValidate(film));
    }

    @Test
    public void testFilmDuration() {
        Film film = Film.builder()
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(2000, 10, 10))
                .duration(0)
                .build();

        Assertions.assertThrows(ValidateException.class, () -> validateService.filmValidate(film));
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

        Assertions.assertThrows(ValidateException.class, () -> validateService.userValidate(user));
        Assertions.assertThrows(ValidateException.class, () -> validateService.userValidate(user1));
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

        Assertions.assertThrows(ValidateException.class, () -> validateService.userValidate(user));
        Assertions.assertThrows(ValidateException.class, () -> validateService.userValidate(user1));
    }

    @Test
    public void testUserBirthday() {
        User user = User.builder()
                .email("u@y.ru")
                .login("login")
                .name("name")
                .birthday(LocalDate.now().plusDays(1))
                .build();

        Assertions.assertThrows(ValidateException.class, () -> validateService.userValidate(user));
    }
}
