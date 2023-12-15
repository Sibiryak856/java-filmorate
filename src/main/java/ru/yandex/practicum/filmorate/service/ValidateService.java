package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Service
public class ValidateService {

    private static final LocalDate EARLIEST_RELEASE_DATE = LocalDate.of(1895, 12, 28);


    public void filmValidate(Film film, List<Mpa> mpaList, List<Genre> genresList) {
        if (mpaList.stream().noneMatch(mpa -> film.getMpa().getId() == mpa.getId())) {
            throw new ValidateException("The film's mpa is not valid");
        }
        Set<Genre> filmGenres = film.getGenres();
        if (filmGenres != null && !filmGenres.isEmpty()) {
            film.getGenres().forEach(genre -> {
                if (genresList.stream().noneMatch(g -> genre.getId() == g.getId())) {
                    throw new ValidateException("The film's genreId is not valid");
                }
            });
        }
        if (film.getReleaseDate().isBefore(EARLIEST_RELEASE_DATE)) {
            throw new ValidateException("The film's release date is too old");
        }
    }

    public void userValidate(User user) {
        if (user.getLogin().contains(" ")) {
            throw new ValidateException("Login contains spaces");
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidateException("Date of Birthday cannot be later than the current date");
        }
    }
}
