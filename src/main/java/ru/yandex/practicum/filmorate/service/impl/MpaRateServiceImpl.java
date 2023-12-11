package ru.yandex.practicum.filmorate.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.MpaRateDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.MpaRate;
import ru.yandex.practicum.filmorate.service.MpaRateService;

import java.util.List;

@Service
public class MpaRateServiceImpl implements MpaRateService {

    public MpaRateDao mpaRateDao;

    @Autowired
    public MpaRateServiceImpl(MpaRateDao mpaRateDao) {
        this.mpaRateDao = mpaRateDao;
    }

    @Override
    public List<MpaRate> getAll() {
        return mpaRateDao.getAll();
    }

    @Override
    public MpaRate getMpa(Integer id) {
        return mpaRateDao.getMpa(id)
                .orElseThrow(() -> new NotFoundException(String.format("MPA rate id=%d not found", id)));
    }
}
