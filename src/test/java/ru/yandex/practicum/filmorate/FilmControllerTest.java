package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.Controller;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exceptions.FilmValidException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

public class FilmControllerTest {

    private Controller filmController;

    @BeforeEach
    public void setUp() {
        filmController = new FilmController();
    }

    @Test
    public void testEmptyFilmName() {
        Film film = new Film(1, "", "description", LocalDate.of(2000,10,10), 100);

        Assertions.assertThrows(FilmValidException.class, () -> filmController.create(film));
    }

    @Test
    public void testLengthFilmDescription() {
        Film film = new Film(1, "name",
                "descriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescription" +
                        "descriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescription" +
                        "descriptiondescriptiondescriptiondescriptiondescription",
                LocalDate.of(2000,10,10), 100);

        Assertions.assertThrows(FilmValidException.class, () -> filmController.create(film));
    }

    @Test
    public void testFilmReleaseDate() {
        Film film = new Film(1, "name", "description", LocalDate.of(1895,12,27), 100);

        Assertions.assertThrows(FilmValidException.class, () -> filmController.create(film));
    }

    @Test
    public void testFilmDuration() {
        Film film = new Film(1, "name", "description", LocalDate.of(2000,12,27), 0);

        Assertions.assertThrows(FilmValidException.class, () -> filmController.create(film));
    }
}
