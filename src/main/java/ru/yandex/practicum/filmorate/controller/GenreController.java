package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.impl.GenreServiceImpl;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/genres")
public class GenreController {

    private GenreServiceImpl genreService;

    @Autowired
    public GenreController(GenreServiceImpl genreService) {
        this.genreService = genreService;
    }

    @GetMapping
    public List<Genre> getAll() {
        log.info("Request received: GET /genres");
        List<Genre> genres = genreService.getAll();
        log.info("Request GET /genres processed: genres: {}", genres);
        return genres;
    }

    @GetMapping("/{id}")
    public Genre getGenre(@PathVariable Integer id) {
        log.info("Request received: GET /genres id={}", id);
        Genre genre = genreService.getGenre(id);
        log.info("Request GET /genres id={} processed: genre: {}", id, genre);
        return genre;
    }
}
