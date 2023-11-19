package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
        return new ArrayList<>(filmStorage.getAll().values());
    }

    @Override
    public Film getFilm(Integer id) {
        checkFilmId(id);
        Film film = filmStorage.getFilm(Optional.ofNullable(id));
        if (film == null) {
            throw new NotFoundException("Фильм не найден");
        }
        return film;
    }

    @Override
    public Film createFilm(Film film) {
        validateService.filmValidation(film);
        return filmStorage.create(film);
    }

    @Override
    public Film update(Film film) {
        checkFilmId(film.getId());
        validateService.filmValidation(film);
        filmStorage.update(film);
        return filmStorage.getFilm(Optional.ofNullable(film.getId()));
    }

    @Override
    public void updateLike(Integer userId, Integer filmId, RequestMethod method) {
        checkFilmId(filmId);
        if (!userStorage.getAll().containsKey(userId)) {
            throw new NotFoundException(String.format("Пользователь id=%d не найден", userId));
        }
        if (method.equals(DELETE)) {
            filmStorage.removeLike(filmId, userId);
        } else if (method.equals(PUT)) {
            filmStorage.addLike(filmId, userId);
        } else {
            throw new ValidateException("Некорректный запрос действия " + method);
        }
    }

    @Override
    public List<Film> getTopFilms(Integer count) {
        return filmStorage.getTopFilms(count);
    }

    private void checkFilmId(Integer id) {
        if (!getAllForCheck().containsKey(id)) {
            throw new NotFoundException(String.format("Фильм с таким id=%d не существует", id));
        }
    }

    private Map<Integer, Film> getAllForCheck() {
        return filmStorage.getAll();
    }
}
