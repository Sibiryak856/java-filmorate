package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.List;
import java.util.Optional;

public interface GenreDao {

    List<Genre> getAll();

    Optional<Genre> getGenre(Integer id);
}
