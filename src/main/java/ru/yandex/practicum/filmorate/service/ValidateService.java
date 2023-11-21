package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@Service
public class ValidateService {

    private static final LocalDate EARLIEST_RELEASE_DATE = LocalDate.of(1895, 12, 28);

    public void filmValidate(Film film) {
        if (film.getReleaseDate().isBefore(EARLIEST_RELEASE_DATE)) {
            throw new ValidateException("Установлена слишком старая дата выхода фильма");
        }
    }

    public void userValidate(User user) {
        if (user.getLogin().contains(" ")) {
            throw new ValidateException("Логин содержит пробелы");
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidateException("Дата рождения не может быть позже текущей даты");
        }
    }
}
