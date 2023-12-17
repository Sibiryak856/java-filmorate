package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface MpaRateService {

    List<Mpa> getAll();

    Mpa getMpa(Integer id);

}
