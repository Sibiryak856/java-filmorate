package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.Optional;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@Service
public class FilmsService implements FilmService {

    public FilmStorage filmStorage;
    private UserStorage userStorage;
    private final ValidateService validateService;

    @Autowired
    public FilmsService(FilmStorage filmStorage, ValidateService validateService, UserStorage userStorage) {
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
        Optional<Film> film = filmStorage.getFilm(id);
        if (film.isEmpty()) {
            throw new NotFoundException(String.format("Фильм id=%d не найден", id));
        }
        return film.get();
    }

    @Override
    public Film createFilm(Film film) {
        validateService.filmValidate(film);
        return filmStorage.create(film);
    }

    @Override
    public Film update(Film film) {
        if (filmStorage.getFilm(film.getId()).isEmpty()) {
            throw new NotFoundException("Фильм не найден");
        }
        validateService.filmValidate(film);
        filmStorage.update(film);
        return filmStorage.getFilm(film.getId()).get();
    }

    @Override
    public void updateLike(Integer userId, Integer filmId, RequestMethod method) {
        if (filmStorage.getFilm(filmId).isEmpty()) {
            throw new NotFoundException(String.format("Фильм id=%d не найден", filmId));
        } else if (userStorage.getUser(userId).isEmpty()) {
            throw new NotFoundException(String.format("Пользователь id=%d не найден", userId));
        }

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
