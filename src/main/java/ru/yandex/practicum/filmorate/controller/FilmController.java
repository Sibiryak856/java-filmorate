package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.*;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {

    private FilmStorage filmStorage;

    @Autowired
    public FilmController(@Qualifier("memoryFilmStorage") FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    @GetMapping
    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    @GetMapping(value = "/{id}")
    public Film getFilm(@PathVariable Integer id) {
        return filmStorage.getFilm(id);
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        return filmStorage.create(film);
    }

    @PutMapping
    public Film update(@RequestBody Film film) {
        return filmStorage.update(film);
    }
}
