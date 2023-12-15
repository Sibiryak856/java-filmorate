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
import ru.yandex.practicum.filmorate.service.ValidateService;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@Service
public class FilmServiceImpl implements FilmService {

    private FilmDao filmDao;
    private UserDao userDao;
    private MpaDao mpaDao;
    private GenreDao genreDao;
    private final ValidateService validateService;

    @Autowired
    public FilmServiceImpl(FilmDao filmDao,
                           ValidateService validateService,
                           UserDao userDao,
                           MpaDao mpaDao,
                           GenreDao genreDao) {
        this.filmDao = filmDao;
        this.validateService = validateService;
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
        List<Mpa> mpaList = mpaDao.getAll();
        List<Genre> genreList = genreDao.getAll();

        checkMpaAndGenreInDb(mpaList, genreList);
        validateService.filmValidate(film, mpaList, genreList);

        mpaList.stream().filter(mpa -> mpa.getId() == film.getMpa().getId()).forEach(film::setMpa);

        return filmDao.create(film);
    }

    @Override
    public Film update(Film film) {
        Film updatingFilm = filmDao.getFilm(film.getId())
                .orElseThrow(() -> new NotFoundException(("Updating film not found")));
        List<Mpa> mpaList = mpaDao.getAll();
        List<Genre> genreList = genreDao.getAll();

        checkMpaAndGenreInDb(mpaList, genreList);
        validateService.filmValidate(film, mpaList, genreList);

        mpaList.stream().filter(mpa -> mpa.getId() == film.getMpa().getId()).forEach(film::setMpa);

        filmDao.update(film);
        return filmDao.getFilm(film.getId()).get();
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

    private void checkMpaAndGenreInDb(List<Mpa> mpaList, List<Genre> genreList) {
        if (mpaList.isEmpty()) {
            throw new NotFoundException("MPA table is empty");
        }
        if (genreList.isEmpty()) {
            throw new NotFoundException("GENRES table is empty");
        }
    }
}
