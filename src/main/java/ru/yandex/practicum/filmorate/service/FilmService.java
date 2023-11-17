package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.*;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@Service
public class FilmService implements FilmSvc {

    public final FilmStorage filmStorage;
    private final ValidateService validateService;

    @Autowired
    public FilmService(FilmStorage filmStorage, ValidateService validateService) {
        this.filmStorage = filmStorage;
        this.validateService = validateService;
    }


    @Override
    public List<Film> getAll() {
        return new ArrayList<>(filmStorage.getAll().values());
    }

    @Override
    public Film getFilm(Integer id) {
        Map<Integer,Film> films = filmStorage.getAll();
        validateService.filmIdValidate(films, id);
        return filmStorage.getFilm(id);
    }

    @Override
    public Film createFilm(Film film) {
        validateService.filmValidation(film);
        return filmStorage.create(film);
    }

    @Override
    public void update(Film film) {
        validateService.filmIdValidate(filmStorage.getAll(), film.getId());
        validateService.filmValidation(film);
        filmStorage.update(film);
    }

    @Override
    public void updateLike(Integer userId, Integer filmId, RequestMethod method) {
        Film film = getFilm(filmId);
        if (method.equals(DELETE)) {
            filmStorage.removeLike(film, userId);
        } else if (method.equals(PUT)) {
            filmStorage.addLike(film, userId);
        } else {
            throw new ValidateException("Некорректный запрос действия " + method);
        }
    }

    @Override
    public List<Film> getTopFilms(Integer count) {
        try {
            return filmStorage.getTopFilms(count);
        } catch (ValidateException e) {
            throw new NotFoundException(String.format("Некорректный параметр count=%d", count));
        }
    }
}
