package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.MpaRate;

import java.util.List;
import java.util.Optional;

public interface MpaRateDao {

    List<MpaRate> getAll();

    Optional<MpaRate> getMpa(Integer id);
}
