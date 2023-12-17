package ru.yandex.practicum.filmorate.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaRateService;

import java.util.List;

@Service
public class MpaRateServiceImpl implements MpaRateService {

    public MpaDao mpaDao;

    @Autowired
    public MpaRateServiceImpl(MpaDao mpaDao) {
        this.mpaDao = mpaDao;
    }

    @Override
    public List<Mpa> getAll() {
        return mpaDao.getAll();
    }

    @Override
    public Mpa getMpa(Integer id) {
        return mpaDao.getMpa(id)
                .orElseThrow(() -> new NotFoundException(String.format("MPA rate id=%d not found", id)));
    }
}
