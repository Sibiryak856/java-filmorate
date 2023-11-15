package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;

@Service
public class FilmService {

    public FilmStorage filmStorage;

    @Autowired
    public FilmService(@Qualifier("memoryFilmStorage") FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public void addLike(Integer id) {

    }

    public void deleteLike(Integer id) {

    }

    public List<Film> getTopFilms() {
        return null;
    }
}
