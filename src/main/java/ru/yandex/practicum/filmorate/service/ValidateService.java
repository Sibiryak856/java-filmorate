package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@Service
public class ValidateService {

    private static final int DESCRIPTION_MAX_SIZE = 200;
    private static final LocalDate EARLIEST_RELEASE_DATE = LocalDate.of(1895, 12, 28);
    private static final int MIN_FILM_DURATION = 0;

    public void filmValidate(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            throw new ValidateException("Имя фильма не задано");
        } else if (film.getDescription().length() >= DESCRIPTION_MAX_SIZE) {
            throw new ValidateException("Превышен лимит длины строки описания");
        } else if (film.getReleaseDate().isBefore(EARLIEST_RELEASE_DATE)) {
            throw new ValidateException("Установлена слишком старая дата выхода фильма");
        } else if (film.getDuration() <= MIN_FILM_DURATION) {
            throw new ValidateException("Длительность фильма меньше допустимой");
        }
    }

    public void userValidate(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            throw new ValidateException("Введен некорректный адрес электронной почты");
        } else if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            throw new ValidateException("Логин введен некорректно");
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidateException("Дата рождения не может быть позже текущей даты");
        }
    }
}
