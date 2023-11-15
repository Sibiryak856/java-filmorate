package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

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

    @PostMapping
    public Film create(@RequestBody Film film) {
        return filmService.filmStorage.create(film);
    }

    @PutMapping
    public Film update(@RequestBody Film film) {
        return filmService.filmStorage.update(film);
    }
}
