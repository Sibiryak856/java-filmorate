package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.impl.FilmServiceImpl;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.*;

@RestController
@Slf4j
@Validated
@RequestMapping("/films")
public class FilmController {

    private final FilmServiceImpl filmServiceImpl;

    @Autowired
    public FilmController(FilmServiceImpl filmServiceImpl) {
        this.filmServiceImpl = filmServiceImpl;
    }

    @GetMapping
    public List<Film> getAll() {
        log.info("Request received: GET /films");
        List<Film> films = filmServiceImpl.getAll();
        log.info("Request GET /films processed: films: {}", films);
        return films;
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable Integer id) {
        log.info("Request received: GET /films id={}", id);
        Film film = filmServiceImpl.getFilm(id);
        log.info("Request GET /films id={} processed: film: {}", id, film);
        return film;
    }

    @Valid
    @GetMapping("/popular")
    public List<Film> getTopFilms(
            @RequestParam(defaultValue = "10", required = false)
            @Min(1) Integer count
    ) {
        log.info("Request received: GET /films/popular");
        List<Film> topFilms = filmServiceImpl.getTopFilms(count);
        log.info("Request GET /films/popular processed: topfilms: {}", topFilms);
        return topFilms;
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.info("Request received: POST /films: {}", film);
        Film createdFilm = filmServiceImpl.createFilm(film);
        log.info("Request POST /films processed: film is created: {}", createdFilm);
        return createdFilm;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        log.info("Request received: PUT /films: {}", film);
        Film updatedFilm = filmServiceImpl.update(film);
        log.info("Request PUT /films processed: film is updated: {}", updatedFilm);
        return updatedFilm;
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(
            @PathVariable() Integer id,
            @PathVariable() Long userId
    ) {
        log.info("Request received: PUT /id={}/like/userId={}", id, userId);
        filmServiceImpl.updateLike(userId, id, RequestMethod.PUT);
        log.info("Request PUT /id={}/like/userId={} processed: like is added", id, userId);

    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(
            @PathVariable() Integer id,
            @PathVariable() Long userId
    ) {
        log.info("Request received: DELETE /id={}/like/userId={}", id, userId);
        filmServiceImpl.updateLike(userId, id, RequestMethod.DELETE);
        log.info("Request DELETE /id={}/like/userId={} processed: like is removed", id, userId);
    }
}
