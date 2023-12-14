package ru.yandex.practicum.filmorate.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MpaEnum;
import ru.yandex.practicum.filmorate.model.MpaRate;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.ValidateService;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@Service
public class FilmServiceImpl implements FilmService {

    public FilmDao filmDao;
    private UserDao userDao;
    private final ValidateService validateService;

    @Autowired
    public FilmServiceImpl(@Qualifier("filmDbStorage") FilmDao filmDao,
                           ValidateService validateService,
                           @Qualifier("userDbStorage") UserDao userDao) {
        this.filmDao = filmDao;
        this.validateService = validateService;
        this.userDao = userDao;
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
        validateService.filmValidate(film);
        MpaRate mpa = film.getMpa();
        mpa.setName(MpaEnum.parseMpaId(mpa.getId()));
        film.setMpa(mpa);
        return filmDao.create(film);
    }

    @Override
    public Film update(Film film) {
        Film updatingFilm = filmDao.getFilm(film.getId())
                .orElseThrow(() -> new NotFoundException(("Updating film not found")));
        validateService.filmValidate(film);
        MpaRate mpa = film.getMpa();
        mpa.setName(MpaEnum.parseMpaId(mpa.getId()));
        film.setMpa(mpa);
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

}
