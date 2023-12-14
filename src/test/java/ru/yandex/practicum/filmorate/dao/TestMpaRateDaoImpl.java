package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.dao.impl.MpaRateDaoImpl;
import ru.yandex.practicum.filmorate.model.MpaEnum;
import ru.yandex.practicum.filmorate.model.MpaRate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@JdbcTest
public class TestMpaRateDaoImpl {

    private final JdbcTemplate jdbcTemplate;

    private MpaRateDaoImpl mpaRateDao;
    private List<MpaRate> mpaRates;

    @BeforeEach
    public void setUp() {
        mpaRateDao = new MpaRateDaoImpl(jdbcTemplate);
        mpaRates = Arrays.asList(MpaEnum.values()).stream()
                .map(mpa -> (new MpaRate(mpa.getId(), mpa.getName())))
                .collect(Collectors.toList());
    }

    @Test
    public void testGetGenres() {
        List<MpaRate> mpaRateList = mpaRateDao.getAll();

        Assertions.assertThat(mpaRateList)
                .isNotNull()
                .isNotEmpty()
                .usingRecursiveComparison()
                .isEqualTo(mpaRates);
    }

    @Test
    public void testGetFilm() {
        mpaRates.forEach(mpaRate -> {
            Optional<MpaRate> res = mpaRateDao.getMpa(mpaRate.getId());

            Assertions.assertThat(res.get())
                    .usingRecursiveComparison()
                    .isEqualTo(mpaRate);
        });
    }
}
