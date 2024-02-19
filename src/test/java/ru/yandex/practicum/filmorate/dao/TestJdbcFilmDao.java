package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import ru.yandex.practicum.filmorate.dao.impl.JdbcFilmDao;
import ru.yandex.practicum.filmorate.dao.impl.JdbcUserDao;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@JdbcTest
public class TestJdbcFilmDao {

    private final NamedParameterJdbcTemplate jdbc;

    private JdbcFilmDao filmDao;
    private JdbcUserDao userDao;
    private Film film;
    private User user;

    @BeforeEach
    public void setUp() {
        filmDao = new JdbcFilmDao(jdbc);
        userDao = new JdbcUserDao(jdbc);
        film = Film.builder()
                .id(1)
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(1995, 12, 27))
                .duration(100)
                .mpa(new Mpa(1, "G"))
                .genres(new LinkedHashSet<>())
                .build();
        user =  User.builder()
                .id(1L)
                .email("u@y.ru")
                .login("login")
                .name("name")
                .birthday(LocalDate.of(2000,10,10))
                .build();
    }

    @Test
    public void testGetAllFilms() {
        filmDao.create(film);

        List<Film> films = filmDao.getAll();


        Assertions.assertThat(films)
                .isNotNull()
                .isNotEmpty()
                .usingRecursiveComparison()
                .isEqualTo(Arrays.asList(film));
    }

    @Test
    public void testGetFilm() {
        filmDao.create(film);

        Optional<Film> res = filmDao.getFilm(film.getId());

        Assertions.assertThat(res.get())
                .usingRecursiveComparison()
                .isEqualTo(film);
    }

    @Test
    public void testUpdateFilm() {
        filmDao.create(film);
        Film filmToUpdate = film;
        filmToUpdate.setMpa(new Mpa(4, "R"));
        filmDao.update(filmToUpdate);
        film.setMpa(new Mpa(4, "R"));

        Optional<Film> res = filmDao.getFilm(filmToUpdate.getId());

        Assertions.assertThat(res.get())
                .usingRecursiveComparison()
                .isEqualTo(film);
    }

    @Test
    public void testAddLike() {
        filmDao.create(film);
        userDao.create(user);
        filmDao.addLike(film.getId(), user.getId());

        List<Film> topFilms = filmDao.getTopFilms(1);

        Assertions.assertThat(topFilms)
                .isNotNull()
                .isNotEmpty();

        Assertions.assertThat(topFilms.get(0))
                .usingRecursiveComparison()
                .isEqualTo(film);
    }

    @Test
    public void testRemoveLike() {
        Film newFilm = film;
        film.setId(2);
        filmDao.create(film);
        filmDao.create(newFilm);
        userDao.create(user);
        filmDao.addLike(newFilm.getId(), user.getId());

        List<Film> topFilms = filmDao.getTopFilms(1);

        Assertions.assertThat(topFilms)
                .isNotNull()
                .isNotEmpty();

        Assertions.assertThat(topFilms.get(0))
                .usingRecursiveComparison()
                .isEqualTo(newFilm);

        filmDao.removeLike(newFilm.getId(), user.getId());

        List<Film> newTopFilms = filmDao.getTopFilms(1);

        Assertions.assertThat(newTopFilms)
                .isNotNull()
                .isNotEmpty();

        Assertions.assertThat(topFilms.get(0))
                .usingRecursiveComparison()
                .isEqualTo(film);
    }
}
