/*
package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exceptions.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.ValidateService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.time.LocalDate;

public class FilmControllerTest {

    private FilmController filmController;
    private FilmService filmService;
    private InMemoryFilmStorage filmStorage;
    private ValidateService validateService;

    @BeforeEach
    public void setUp() {
        validateService = new ValidateService();
        filmService = new FilmService();
        filmStorage = new InMemoryFilmStorage(validateService, filmService);
        filmController = new FilmController(filmStorage);
    }

    @Test
    public void testEmptyFilmName() {
        Film film = new Film(1, "", "description", LocalDate.of(2000,10,10), 100);

        Assertions.assertThrows(ValidateException.class, () -> filmController.create(film));
    }

    @Test
    public void testLengthFilmDescription() {
        Film film = new Film(1, "name",
                "descriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescription" +
                        "descriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescription" +
                        "descriptiondescriptiondescriptiondescriptiondescription",
                LocalDate.of(2000,10,10), 100);

        Assertions.assertThrows(ValidateException.class, () -> filmController.create(film));
    }

    @Test
    public void testFilmReleaseDate() {
        Film film = new Film(1, "name", "description", LocalDate.of(1895,12,27), 100);

        Assertions.assertThrows(ValidateException.class, () -> filmController.create(film));
    }

    @Test
    public void testFilmDuration() {
        Film film = new Film(1, "name", "description", LocalDate.of(2000,12,27), 0);

        Assertions.assertThrows(ValidateException.class, () -> filmController.create(film));
    }
}
*/
