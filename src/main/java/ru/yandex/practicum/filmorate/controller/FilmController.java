package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmsService;

import javax.validation.constraints.Min;
import java.util.*;

@RestController
@Slf4j
@Validated
@RequestMapping("/films")
public class FilmController {

    private final FilmsService filmsService;

    @Autowired
    public FilmController(FilmsService filmsService) {
        this.filmsService = filmsService;
    }

    @GetMapping
    public List<Film> getAll() {
        log.info("Request received: GET /films");
        List<Film> films = filmsService.getAll();
        log.info("Request GET /films processed: films: {}", films);
        return films;
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable Integer id) {
        log.info("Request received: GET /films id={}", id);
        Film film = filmsService.getFilm(id);
        log.info("Request GET /films id={} processed: film: {}", id, film);
        return film;
    }

    @GetMapping("/popular")
    public List<Film> getTopFilms(
            @RequestParam(defaultValue = "10", required = false)
            @Min(value = 1, message = "Count could be bigger than 0")
            Integer count
    ) {
        log.info("Request received: GET /films/popular");
        List<Film> topFilms = filmsService.getTopFilms(count);
        log.info("Request GET /films/popular processed: topfilms: {}", topFilms);
        return topFilms;
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        log.info("Request received: POST /films: {}", film);
        Film createdFilm = filmsService.createFilm(film);
        log.info("Request POST /films processed: film is created: {}", createdFilm);
        return createdFilm;
    }

    @PutMapping
    public Film update(@RequestBody Film film) {
        log.info("Request received: PUT /films: {}", film);
        Film updatedFilm = filmsService.update(film);
        log.info("Request PUT /films processed: film is updated: {}", updatedFilm);
        return updatedFilm;
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(
            @PathVariable() Integer id,
            @PathVariable() Integer userId
    ) {
        log.info("Request received: PUT /id={}/like/userId={}", id, userId);
        filmsService.updateLike(userId, id, RequestMethod.PUT);
        log.info("Request PUT /id={}/like/userId={} processed: like is added", id, userId);

    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(
            @PathVariable() Integer id,
            @PathVariable() Integer userId
    ) {
        log.info("Request received: DELETE /id={}/like/userId={}", id, userId);
        filmsService.updateLike(userId, id, RequestMethod.DELETE);
        log.info("Request DELETE /id={}/like/userId={} processed: like is removed", id, userId);
    }
}
