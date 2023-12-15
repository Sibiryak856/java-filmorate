package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import ru.yandex.practicum.filmorate.dao.impl.JdbcMpaDao;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@JdbcTest
public class TestJdbcMpaDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    private JdbcMpaDao mpaDao;
    private List<Mpa> mpaList;

    @BeforeEach
    public void setUp() {
        mpaDao = new JdbcMpaDao(jdbcTemplate);
        mpaList = Arrays.asList(
                new Mpa(1, "G"),
                new Mpa(2, "PG"),
                new Mpa(3, "PG-13"),
                new Mpa(4, "R"),
                new Mpa(5, "NC-17")
                );
    }

    @Test
    public void testGetGenres() {
        List<Mpa> mpaList = mpaDao.getAll();

        Assertions.assertThat(mpaList)
                .isNotNull()
                .isNotEmpty()
                .usingRecursiveComparison()
                .isEqualTo(this.mpaList);
    }

    @Test
    public void testGetFilm() {
        mpaList.forEach(mpaRate -> {
            Optional<Mpa> res = mpaDao.getMpa(mpaRate.getId());

            Assertions.assertThat(res.get())
                    .usingRecursiveComparison()
                    .isEqualTo(mpaRate);
        });
    }
}
