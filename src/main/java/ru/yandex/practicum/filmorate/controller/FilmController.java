package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.IncorrectIdException;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.*;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public List<Film> getAll() {
        return filmService.filmStorage.getAll();
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable Integer id) {
        return filmService.filmStorage.getFilm(id);
    }

    @GetMapping("/popular")
    public List<Film> getTopFilms(
            @RequestParam(defaultValue = "10", required = false) Integer count
    ) {
        if (count <= 0) {
            String error = String.format("Некорректный параметр count=%d", count);
            log.error(error);
            throw new ValidateException(error);
        }
        return filmService.getTopFilms(count);
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        return filmService.filmStorage.create(film);
    }

    @PutMapping
    public Film update(@RequestBody Film film) {
        return filmService.filmStorage.update(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(
            @PathVariable() Integer id,
            @PathVariable() Integer userId
    ) {
        if (id < 0 || userId < 0) {
            String error = "Id не может быть отрицательным числом: %d";
            log.error(error);
            throw new IncorrectIdException(error);
        }
        filmService.updateLike(userId, id, RequestMethod.PUT);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(
            @PathVariable() Integer id,
            @PathVariable() Integer userId
    ) {
        if (id < 0 || userId < 0) {
            String error = "Id не может быть отрицательным числом: %d";
            log.error(error);
            throw new IncorrectIdException(error);
        }
        filmService.updateLike(userId, id, RequestMethod.DELETE);
    }
}
