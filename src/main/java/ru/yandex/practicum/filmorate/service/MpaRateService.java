package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.MpaRate;

import java.util.List;

public interface MpaRateService {

    List<MpaRate> getAll();

    MpaRate getMpa(Integer id);

}
