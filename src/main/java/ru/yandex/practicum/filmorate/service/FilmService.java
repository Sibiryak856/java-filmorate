package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@Service
@Slf4j
public class FilmService {

    public final FilmStorage filmStorage;

    @Autowired
    public FilmService(@Qualifier("memoryFilmStorage") FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public void updateLike(Integer userId, Integer filmId, RequestMethod method) {
        Film film = filmStorage.getFilm(filmId);
        Set<Integer> likes = film.getLikes();
        if (method.equals(DELETE)) {
            log.info("Пользователь id {} убрал лайк фильму id {}", userId, filmId);
            likes.remove(userId);
            film.setLikes(likes);
        } else if (method.equals(PUT)) {
            log.info("Пользователь id {} поставил лайк фильму id {}", userId, filmId);
            likes.add(userId);
            film.setLikes(likes);
        } else {
            String msg = "Некорректный запрос действия " + method;
            log.error(msg);
            throw new ValidateException(msg);
        }
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

    public List<Film> getTopFilms(Integer count) {
        List<Film> topFilmsById = filmStorage.getAll().stream()
                .sorted(new TopFilmComparator())
                .limit(count)
                .collect(Collectors.toList());
        log.info("Обработан запрос вывода топ {} фильмов", count);
        return topFilmsById;
    }

    private class TopFilmComparator implements Comparator<Film> {
        @Override
        public int compare(Film f1, Film f2) {
            if (f1.getLikes().size() > f2.getLikes().size()) {
                return -1;
            } else if (f1.getLikes().size() < f2.getLikes().size()) {
                return 1;
            }
            return 0;
        }
    }
}
