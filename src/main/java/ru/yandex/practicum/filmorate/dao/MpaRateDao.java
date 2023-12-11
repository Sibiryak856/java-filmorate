package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.List;
import java.util.Optional;

public interface MpaRateDao {

    List<Mpa> getAll();

    Optional<MpaRating> getMpaRate(Integer id);
}
