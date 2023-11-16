package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.ValidateService;

import java.util.*;

@Component("memoryFilmStorage")
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    private final ValidateService validateService;
    private int filmId = 0;
    private final Map<Integer, Film> films = new HashMap<>();

    @Autowired
    public InMemoryFilmStorage(ValidateService validateService) {
        this.validateService = validateService;
    }


    @Override
    public List<Film> getAll() {
        log.info("Обработан запрос получения списка фильмов: {}", films);
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getFilm(Integer id) {
        if (!films.containsKey(id)) {
            String error = "Фильма с таким id не существует";
            log.error(error);
            throw new NoSuchElementException(error);
        }
        log.info("Обработан запрос полчения фильма id {}", films.get(id));
        return films.get(id);
    }

    @Override
    public Film create(Film film) {
        validateService.filmValidation(film);
        film.setId(++filmId);
        log.info("Создан фильм: {}", film);
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film update(Film film) {
        if (film.getId() == null) {
            String error = "Не указан id фильма";
            log.error(error);
            throw new ValidateException(error);
        }
        validateService.filmValidation(film);
        if (!films.containsKey(film.getId())) {
            String error = "Фильма с таким id не существует";
            log.error(error);
            throw new NoSuchElementException(error);
        }
        log.info("Фильм id {} обновлен: {}", film.getId(), film);
        films.put(film.getId(), film);
        return film;
    }
}
