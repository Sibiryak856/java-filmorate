package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.impl.MpaRateServiceImpl;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/mpa")
public class MpaRateController {

    private MpaRateServiceImpl mpaRateService;

    @Autowired
    public MpaRateController(MpaRateServiceImpl mpaRateService) {
        this.mpaRateService = mpaRateService;
    }

    @GetMapping
    public List<Mpa> getAll() {
        log.info("Request received: GET /mpa");
        List<Mpa> mpas = mpaRateService.getAll();
        log.info("Request GET /mpa processed: mpaRates: {}", mpas);
        return mpas;
    }

    @GetMapping("/{id}")
    public Mpa getGenre(@PathVariable Integer id) {
        log.info("Request received: GET /mpa id={}", id);
        Mpa mpa = mpaRateService.getMpa(id);
        log.info("Request GET /mpa id={} processed: mpa: {}", id, mpa);
        return mpa;
    }
}
