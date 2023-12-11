package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@Service
public class FilmServiceImpl implements FilmService {

    public FilmStorage filmStorage;
    private UserStorage userStorage;
    private final ValidateService validateService;

    @Autowired
    public FilmServiceImpl(@Qualifier("filmDbStorage") FilmStorage filmStorage,
                           ValidateService validateService,
                           @Qualifier("userDbStorage") UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.validateService = validateService;
        this.userStorage = userStorage;
    }

    @Override
    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    @Override
    public Film getFilm(Integer id) {
        return filmStorage.getFilm(id)
                .orElseThrow(() -> new NotFoundException(String.format("Фильм id=%d не найден", id)));
    }

    @Override
    public Film createFilm(Film film) {
        validateService.filmValidate(film);
        if (film.getGenres() == null) {
            film.setGenres(new ArrayList<>());
        }
        return filmStorage.create(film);
    }

    @Override
    public Film update(Film film) {
        Film updatingFilm = filmStorage.getFilm(film.getId())
                .orElseThrow(() -> new NotFoundException(("Обновляемый фильм не найден")));
        validateService.filmValidate(film);
        if (film.getGenres() == null) {
            film.setGenres(new ArrayList<>());
        }
        filmStorage.update(film);
        return filmStorage.getFilm(film.getId()).get();
    }

    @Override
    public void updateLike(Long userId, Integer filmId, RequestMethod method) {
        Film updatingFilm = filmStorage.getFilm(filmId)
                .orElseThrow(() -> new NotFoundException(String.format("Фильм id=%d не найден", filmId)));
        User updatingUser = userStorage.getUser(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь id=%d не найден", userId)));

        if (method == DELETE) {
            filmStorage.removeLike(filmId, userId);
        } else if (method == PUT) {
            filmStorage.addLike(filmId, userId);
        }
    }

    @Override
    public List<Film> getTopFilms(Integer count) {
        return filmStorage.getTopFilms(count);
    }
}
