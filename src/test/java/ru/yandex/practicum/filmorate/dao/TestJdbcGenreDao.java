package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import ru.yandex.practicum.filmorate.dao.impl.JdbcGenreDao;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@JdbcTest
public class TestJdbcGenreDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    private JdbcGenreDao genreDao;
    private List<Genre> genres;

    @BeforeEach
    public void setUp() {
        genreDao = new JdbcGenreDao(jdbcTemplate);
        genres = Arrays.asList(
                new Genre(1, "Комедия"),
                new Genre(2, "Драма"),
                new Genre(3, "Мультфильм"),
                new Genre(4, "Триллер"),
                new Genre(5, "Документальный"),
                new Genre(6, "Боевик")
        );
    }

    @Test
    public void testGetGenres() {
        List<Genre> genreList = genreDao.getAll();

        Assertions.assertThat(genreList)
                .isNotNull()
                .isNotEmpty()
                .usingRecursiveComparison()
                .isEqualTo(genres);
    }

    @Test
    public void testGetFilm() {
        genres.forEach(genre -> {
            Optional<Genre> res = genreDao.getGenre(genre.getId());

            Assertions.assertThat(res.get())
                    .usingRecursiveComparison()
                    .isEqualTo(genre);
        });
    }
}
