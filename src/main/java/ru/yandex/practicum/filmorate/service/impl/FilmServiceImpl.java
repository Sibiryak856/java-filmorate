package ru.yandex.practicum.filmorate.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@Service
public class FilmServiceImpl implements FilmService {

    private FilmDao filmDao;
    private UserDao userDao;
    private MpaDao mpaDao;
    private GenreDao genreDao;

    @Autowired
    public FilmServiceImpl(FilmDao filmDao,
                           UserDao userDao,
                           MpaDao mpaDao,
                           GenreDao genreDao) {
        this.filmDao = filmDao;
        this.userDao = userDao;
        this.mpaDao = mpaDao;
        this.genreDao = genreDao;
    }

    @Override
    public List<Film> getAll() {
        return filmDao.getAll();
    }

    @Override
    public Film getFilm(Integer id) {
        return filmDao.getFilm(id)
                .orElseThrow(() -> new NotFoundException(String.format("Film id=%d not found", id)));
    }

    @Override
    public Film createFilm(Film film) {
        checkFilm(film);
        film.setMpa(mpaDao.getMpa(film.getMpa().getId()).get());
        return filmDao.create(film);
    }

    @Override
    public Film update(Film film) {
        checkFilm(film);
        film.setMpa(mpaDao.getMpa(film.getMpa().getId()).get());
        filmDao.update(film);
        return filmDao.getFilm(film.getId()).get();
    }

    private void checkFilm(Film film) {
        Film checkingFilm = filmDao.getFilm(film.getId())
                .orElseThrow(() -> new NotFoundException(("Updating film not found")));

        Mpa mpa = mpaDao.getMpa(film.getMpa().getId())
                .orElseThrow(() ->
                        new NotFoundException(String.format("Film's mpa %d not found", film.getMpa().getId())));

        Set<Genre> filmGenres = film.getGenres();
        Set<Genre> filmGenresFromDataBase = new HashSet<>();
        if (filmGenres != null && !filmGenres.isEmpty()) {
            for (Genre genre : filmGenres) {
                filmGenresFromDataBase.add(genreDao.getGenre(genre.getId()).get());
            }
            if (filmGenres.size() != filmGenresFromDataBase.size()) {
                throw new NotFoundException("Not all genres are found in the database");
            }
        }
    }

    @Override
    public void updateLike(Long userId, Integer filmId, RequestMethod method) {
        Film updatingFilm = filmDao.getFilm(filmId)
                .orElseThrow(() -> new NotFoundException(String.format("Film id=%d not found", filmId)));
        User updatingUser = userDao.getUser(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User id=%d not found", userId)));

        if (method == DELETE) {
            filmDao.removeLike(filmId, userId);
        } else if (method == PUT) {
            filmDao.addLike(filmId, userId);
        }
    }

    @Override
    public List<Film> getTopFilms(Integer count) {
        return filmDao.getTopFilms(count);
    }
}
