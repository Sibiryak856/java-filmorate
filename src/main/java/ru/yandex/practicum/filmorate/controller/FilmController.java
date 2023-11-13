package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.FilmValidException;
import ru.yandex.practicum.filmorate.exceptions.UserValidException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.*;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController implements Controller<Film> {
    private static final int DESCRIPTION_MAX_SIZE = 200;
    private static final LocalDate EARLIEST_RELEASE_DATE = LocalDate.of(1895, 12, 28);
    private static final int MIN_FILM_DURATION = 0;

    private int filmId = 0;
    private final Map<Integer, Film> films = new HashMap<>();

    @Override
    @GetMapping
    public List<Film> getAll() {
        log.info("GET /films {}", films.size());
        return new ArrayList<>(films.values());
    }

    @Override
    @GetMapping(value = "/{id}")
    public Film get(@PathVariable Integer id) {
        log.info("Get /films {}", films.get(id));
        if (films.containsKey(id)) {
            return films.get(id);
        } else {
            String error = "Фильма с таким id не существует";
            log.error(error);
            throw new UserValidException(error);
        }
    }

    @Override
    @PostMapping
    public Film create(@RequestBody Film film) throws RuntimeException {
        filmValidation(film);
        film.setId(++filmId);
        log.info("POST /films {}", film);
        films.put(film.getId(), film);
        return film;
    }

    @Override
    @PutMapping
    public Film update(@RequestBody Film film) throws RuntimeException {
        filmValidation(film);
        if (films.containsKey(film.getId())) {
            log.info("PUT /films {} updated: {}",  film.getId(), film);
            films.put(film.getId(), film);
        } else {
            String error = "Фильма с таким id не существует";
            log.error(error);
            throw new FilmValidException(error);
        }
        return film;
    }

    private static void filmValidation(Film film) throws RuntimeException {
        if (film.getName() == null || film.getName().isBlank()) {
            String errorMessage = "Имя фильма не задано";
            log.error(errorMessage);
            throw new FilmValidException(errorMessage);
        } else if (film.getDescription().length() >= DESCRIPTION_MAX_SIZE) {
            String errorMessage = "Превышен лимит длины строки описания";
            log.error(errorMessage);
            throw new FilmValidException(errorMessage);
        } else if (film.getReleaseDate().isBefore(EARLIEST_RELEASE_DATE)) {
            String errorMessage = "Установлена слишком старая дата выхода фильма";
            log.error(errorMessage);
            throw new FilmValidException(errorMessage);
        } else if (film.getDuration() <= MIN_FILM_DURATION) {
            String errorMessage = "Длительность фильма меньше допустимой";
            log.error(errorMessage);
            throw new FilmValidException(errorMessage);
        }
    }
}
