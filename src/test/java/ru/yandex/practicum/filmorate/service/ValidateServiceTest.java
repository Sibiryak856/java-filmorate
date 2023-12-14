package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRate;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Arrays;

public class ValidateServiceTest {

    private ValidateService validateService;

    @BeforeEach
    public void setUp() {
        validateService = new ValidateService();
    }

    @Test
    public void testFilmHaveMpa() {
        Film film = Film.builder()
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(1995, 12, 27))
                .duration(100)
                .build();

        Assertions.assertThrows(ValidateException.class, () -> validateService.filmValidate(film));
    }

    @Test
    public void testFilmMpaIdAccessible() {
        Film film = Film.builder()
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(1995, 12, 27))
                .duration(100)
                .mpa(new MpaRate(6))
                .build();

        Assertions.assertThrows(ValidateException.class, () -> validateService.filmValidate(film));

        film.setMpa(new MpaRate(0));

        Assertions.assertThrows(ValidateException.class, () -> validateService.filmValidate(film));
    }

    @Test
    public void testFilmGenreIdAccessible() {
        Film film = Film.builder()
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(1995, 12, 27))
                .duration(100)
                .mpa(new MpaRate(1))
                .genres(Arrays.asList(new Genre(7)))
                .build();

        Assertions.assertThrows(ValidateException.class, () -> validateService.filmValidate(film));

        film.setGenres(Arrays.asList(new Genre(0)));

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
    public void testUserLogin() {
        User user = User.builder()
                .email("u@y.ru")
                .login("log in")
                .name("name")
                .birthday(LocalDate.of(2000,10,10))
                .build();

        Assertions.assertThrows(ValidateException.class, () -> validateService.userValidate(user));
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
