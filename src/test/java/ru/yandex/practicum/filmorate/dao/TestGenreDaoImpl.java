package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.dao.impl.GenreDaoImpl;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.GenreEnum;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@JdbcTest
public class TestGenreDaoImpl {

    private final JdbcTemplate jdbcTemplate;

    private GenreDaoImpl genreDao;
    private List<Genre> genres;

    @BeforeEach
    public void setUp() {
        genreDao = new GenreDaoImpl(jdbcTemplate);
        genres = Arrays.asList(GenreEnum.values()).stream()
                .map(genre -> (new Genre(genre.getId(), genre.getName())))
                .collect(Collectors.toList());
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
