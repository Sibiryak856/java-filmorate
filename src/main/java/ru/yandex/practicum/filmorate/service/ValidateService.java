package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exceptions.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@Slf4j
public class ValidateService {

    private static final int DESCRIPTION_MAX_SIZE = 200;
    private static final LocalDate EARLIEST_RELEASE_DATE = LocalDate.of(1895, 12, 28);
    private static final int MIN_FILM_DURATION = 0;

    public void filmValidation(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            String errorMessage = "Имя фильма не задано";
            log.error(errorMessage);
            throw new ValidateException(errorMessage);
        } else if (film.getDescription().length() >= DESCRIPTION_MAX_SIZE) {
            String errorMessage = "Превышен лимит длины строки описания";
            log.error(errorMessage);
            throw new ValidateException(errorMessage);
        } else if (film.getReleaseDate().isBefore(EARLIEST_RELEASE_DATE)) {
            String errorMessage = "Установлена слишком старая дата выхода фильма";
            log.error(errorMessage);
            throw new ValidateException(errorMessage);
        } else if (film.getDuration() <= MIN_FILM_DURATION) {
            String errorMessage = "Длительность фильма меньше допустимой";
            log.error(errorMessage);
            throw new ValidateException(errorMessage);
        }
    }

    public void userValidate(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            String errorMessage = "Введен некорректный адрес электронной почты";
            log.error(errorMessage);
            throw new ValidateException(errorMessage);
        } else if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            String errorMessage = "Логин введен некорректно";
            log.error(errorMessage);
            throw new ValidateException(errorMessage);
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            String errorMessage = "Дата рождения не может быть позже текущей даты";
            log.error(errorMessage);
            throw new ValidateException(errorMessage);
        }
    }
}
