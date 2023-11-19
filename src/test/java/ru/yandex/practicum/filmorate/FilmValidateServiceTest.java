package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmsService;
import ru.yandex.practicum.filmorate.service.ValidateService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;

public class FilmValidateServiceTest {

    private FilmController filmController;
    private FilmsService filmsService;
    private FilmStorage filmStorage;
    private UserStorage userStorage;
    private ValidateService validateService;

    @BeforeEach
    public void setUp() {
        validateService = new ValidateService();
        filmStorage = new InMemoryFilmStorage();
        userStorage = new InMemoryUserStorage();
        filmsService = new FilmsService(filmStorage, validateService, userStorage);
        filmController = new FilmController(filmsService);
    }

    @Test
    public void testEmptyFilmName() {
        Film film = Film.builder()
                .name("")
                .description("description")
                .releaseDate(LocalDate.of(2000, 10, 10))
                .duration(100)
                .build();

        Assertions.assertThrows(ValidateException.class, () -> filmController.create(film));
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

        Assertions.assertThrows(ValidateException.class, () -> filmController.create(film));
    }

    @Test
    public void testFilmReleaseDate() {
        Film film = Film.builder()
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(1895, 12, 27))
                .duration(100)
                .build();

        Assertions.assertThrows(ValidateException.class, () -> filmController.create(film));
    }

    @Test
    public void testFilmDuration() {
        Film film = Film.builder()
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(2000, 10, 10))
                .duration(0)
                .build();

        Assertions.assertThrows(ValidateException.class, () -> filmController.create(film));
    }
}
