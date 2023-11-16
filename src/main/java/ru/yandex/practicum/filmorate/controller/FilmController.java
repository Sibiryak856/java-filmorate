package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.ValidateService;

import java.util.*;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {

    private int filmId = 0;
    private final Map<Integer, Film> films = new HashMap<>();

    ValidateService validateService = new ValidateService();

    @GetMapping
    public List<Film> getAll() {
        log.info("GET /films {}", films.size());
        return new ArrayList<>(films.values());
    }

    @GetMapping(value = "/{id}")
    public Film get(@PathVariable Integer id) {
        log.info("Get /films {}", films.get(id));
        if (!films.containsKey(id)) {
            String error = "Фильма с таким id не существует";
            log.error(error);
            throw new ValidateException(error);
        }
        return films.get(id);
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        validateService.filmValidation(film);
        film.setId(++filmId);
        log.info("POST /films {}", film);
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film update(@RequestBody Film film) {
        if (film.getId() == null) {
            String error = "Не указан id фильма";
            log.error(error);
            throw new ValidateException(error);
        }
        validateService.filmValidation(film);
        if (!films.containsKey(film.getId())) {
            String error = "Фильма с таким id не существует";
            log.error(error);
            throw new ValidateException(error);
        }
        log.info("PUT /films {} updated: {}", film.getId(), film);
        films.put(film.getId(), film);
        return film;
    }
}
