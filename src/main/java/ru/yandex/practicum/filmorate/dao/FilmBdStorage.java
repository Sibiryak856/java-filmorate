package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;
import java.util.Optional;

@Component("filmDb")
@RequiredArgsConstructor
public class FilmBdStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    
    @Override
    public List<Film> getAll() {
        return null;
    }

    @Override
    public Optional<Film> getFilm(Integer id) {
        return Optional.empty();
    }

    @Override
    public Film create(Film film) {
        return null;
    }

    @Override
    public void update(Film film) {

    }

    @Override
    public void addLike(Integer filmId, Long userId) {

    }

    @Override
    public void removeLike(Integer filmId, Long userId) {

    }

    @Override
    public List<Film> getTopFilms(Integer count) {
        return null;
    }
}
