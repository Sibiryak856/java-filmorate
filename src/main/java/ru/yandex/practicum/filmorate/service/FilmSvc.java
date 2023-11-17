package ru.yandex.practicum.filmorate.service;

import org.springframework.web.bind.annotation.RequestMethod;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmSvc {

    List<Film> getAll();

    Film getFilm(Integer id);

    Film createFilm(Film film);

    void update(Film film);

    void updateLike(Integer userId, Integer filmId, RequestMethod method);

    List<Film> getTopFilms(Integer count);

}
