package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.*;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {

    private FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public List<Film> getAll() {
        return filmService.filmStorage.getAll();
    }

    @GetMapping(value = "/{id}")
    public Film getFilm(@PathVariable Integer id) {
        return filmService.filmStorage.getFilm(id);
    }

    @GetMapping("/topFilms")
    public List<Film> getTopFilms() {
        return filmService.getTopFilms();
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        return filmService.filmStorage.create(film);
    }

    @PutMapping
    public Film update(@RequestBody Film film) {
        return filmService.filmStorage.update(film);
    }

    @PutMapping("/like")
    public Film updateLike(
            @RequestParam() Integer userId,
            @RequestParam() Integer filmId,
            @RequestParam() String action
    ) {
        return filmService.updateLike(userId, filmId, action);
    }
}
