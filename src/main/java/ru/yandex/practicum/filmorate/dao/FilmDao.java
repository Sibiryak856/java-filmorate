package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmDao {

    List<Film> getAll();

    Optional<Film> getFilm(Integer id);

    Film create(Film film);

    void update(Film film);

    void addLike(Integer filmId, Long userId);

    void removeLike(Integer filmId, Long userId);

    List<Film> getTopFilms(Integer count);

}
