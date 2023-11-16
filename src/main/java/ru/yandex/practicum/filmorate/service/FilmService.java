package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;
import java.util.Set;

import static ru.yandex.practicum.filmorate.Constants.LIKE_ACTIONS;
import static ru.yandex.practicum.filmorate.Constants.LIKE_CANCELLATION;

@Service
public class FilmService {

    public FilmStorage filmStorage;

    @Autowired
    public FilmService(@Qualifier("memoryFilmStorage") FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Film updateLike(Integer userId, Integer filmId, String action) {
        Film film = filmStorage.getFilm(filmId);
        if (!LIKE_ACTIONS.contains(action)) {
            throw new IncorrectParameterException(action);
        }
        Set<Integer> likes = film.getLikes();
        if (action.equals(LIKE_CANCELLATION)) {
            likes.remove(userId);
            film.setLikes(likes);
            return film;
        }
        likes.add(userId);
        film.setLikes(likes);
        return film;
    }
    /*public void addLike(Integer id) {
        Film film = filmStorage.getFilm(id);
        Set<Integer> likes = film.getLikes();
        likes.add(id);
        film.setLikes(likes);
    }

    public void deleteLike(Integer id) {
        Film film = filmStorage.getFilm(id);
        Set<Integer> likes = film.getLikes();
        likes.add(id);
        film.setLikes(likes);
    }*/

    public List<Film> getTopFilms() {
        return filmStorage.getTopTenFilmsByLikes();
    }

}
