package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@Service
public class ValidateService {

    private static final LocalDate EARLIEST_RELEASE_DATE = LocalDate.of(1895, 12, 28);

    public void filmValidate(Film film) {
        if (film.getMpa() == null) {
            throw new ValidateException("MPA_ID movie not set");
        } else if (!Mpa.checkMpaID(film.getMpa().getId())) {
            throw new ValidateException("Unknown movie MPA_ID");
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
