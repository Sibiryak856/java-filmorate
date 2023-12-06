package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component("filmDb")
@RequiredArgsConstructor
public class FilmBdStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    
    @Override
    public List<Film> getAll() {
        return jdbcTemplate.query(
                "select * from FILMS",
                (rs, rowNum) -> makeFilm(rs));
    }

    @Override
    public Optional<Film> getFilm(Integer id) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(
                "select * from FILMS where FILM_ID = ?",
                (rs, rowNum) -> makeFilm(rs),
                id));
    }

    @Override
    public Film create(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("FILMS")
                .usingGeneratedKeyColumns("FILM_ID");
        film.setId(simpleJdbcInsert.executeAndReturnKey(film.toMap()).intValue());
        return null;
    }

    @Override
    public void update(Film film) {

    }

    @Override
    public void addLike(Integer filmId, Long userId) {

    }

    @Override
    public void removeLike(Integer filmId, Long userId) {

    }

    @Override
    public List<Film> getTopFilms(Integer count) {
        return null;
    }

    private Film makeFilm(ResultSet rs) throws SQLException {
        /*if (!MPA.checkMpaID(rs.getInt("MPA_ID"))) {
            throw new NotFoundException("Жанр не найден")
        }*/
        // проверка на соответствие enum в service => create
        return Film.builder()
                .id(rs.getInt("FILM_ID"))
                .name(rs.getString("FILM_NAME"))
                .description(rs.getString("DESCRIPTION"))
                .releaseDate(rs.getDate("RELEASE_DATE").toLocalDate())
                .duration(rs.getInt("DURATION"))
                .mpaId(rs.getInt("MPA_ID"))
                .filmGenresId(Arrays.asList(rs.getArray("FILM_GENRES").getBaseType()))
                .build();
    }
}
