package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;

public class ValidateServiceTest {

    private ValidateService validateService;
    private List<Mpa> mpaList;
    private List<Genre> genres;
    private Film film;
    private User user;

    @BeforeEach
    public void setUp() {
        validateService = new ValidateService();
        genres = Arrays.asList(
                new Genre(1, "Комедия"),
                new Genre(2, "Драма"),
                new Genre(3, "Мультфильм"),
                new Genre(4, "Триллер"),
                new Genre(5, "Документальный"),
                new Genre(6, "Боевик")
        );
        mpaList = Arrays.asList(
                new Mpa(1, "G"),
                new Mpa(2, "PG"),
                new Mpa(3, "PG-13"),
                new Mpa(4, "R"),
                new Mpa(5, "NC-17")
        );
        film = Film.builder()
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(1995, 12, 27))
                .duration(100)
                .mpa(new Mpa(1))
                .build();
        user = User.builder()
                .email("u@y.ru")
                .login("login")
                .name("name")
                .birthday(LocalDate.of(2000,10,10))
                .build();
    }

    @Test
    public void testFilmMpaIdIsValid() {
        film.setMpa(new Mpa(6));

        Assertions.assertThrows(ValidateException.class, () -> validateService.filmValidate(film, mpaList, genres));

        film.setMpa(new Mpa(0));

        Assertions.assertThrows(ValidateException.class, () -> validateService.filmValidate(film, mpaList, genres));
    }

    @Test
    public void testFilmGenreIsValid() {
        film.setGenres(new LinkedHashSet<>(Arrays.asList(new Genre(7))));

        Assertions.assertThrows(ValidateException.class, () -> validateService.filmValidate(film, mpaList, genres));

        film.setGenres(new LinkedHashSet<>(Arrays.asList(new Genre(0))));

        Assertions.assertThrows(ValidateException.class, () -> validateService.filmValidate(film, mpaList, genres));
    }

    @Test
    public void testFilmReleaseDate() {
        film.setReleaseDate(LocalDate.of(1895, 12, 27));

        Assertions.assertThrows(ValidateException.class, () -> validateService.filmValidate(film, mpaList, genres));
    }

    @Test
    public void testUserLogin() {
        user.setLogin("log in");

        Assertions.assertThrows(ValidateException.class, () -> validateService.userValidate(user));
    }

    @Test
    public void testUserBirthday() {
        user.setBirthday(LocalDate.now().plusDays(1));

        Assertions.assertThrows(ValidateException.class, () -> validateService.userValidate(user));
    }
}
