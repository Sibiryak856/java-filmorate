package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface FilmStorage {

    Map<Integer, Film> getAll();

    Film getFilm(Optional<Integer> id);

    Film create(Film film);

    void update(Film film);

    void addLike(Integer filmId, Integer userId);

    void removeLike(Integer filmId, Integer userId);

    List<Film> getTopFilms(Integer count);

    Set<Integer> getFilmLikes(Integer id);

}
