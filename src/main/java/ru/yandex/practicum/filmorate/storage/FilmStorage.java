package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Map;

public interface FilmStorage {

    Map<Integer, Film> getAll();

    Film getFilm(Integer id);

    Film create(Film film);

    void update(Film film);

    void addLike(Film film, Integer userId);

    void removeLike(Film film, Integer userId);

    List<Film> getTopFilms(Integer count);

}
