package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRate;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository("filmDbStorage")
@RequiredArgsConstructor
public class FilmBdStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    
    @Override
    public List<Film> getAll() {
        return jdbcTemplate.query("select f.FILM_ID, f.FILM_NAME, f.DESCRIPTION, f.RELEASE_DATE, f.DURATION, " +
                        "f.MPA_ID, m.MPA_NAME, g.FILM_GENRES " +
                        "from FILMS as f " +
                        "LEFT JOIN (SELECT FILM_ID, string_agg(GENRE_ID, ',') AS FILM_GENRES " +
                        "FROM FILM_GENRES " +
                        "GROUP BY FILM_ID) AS g " +
                        "ON g.FILM_ID = f.FILM_ID " +
                        "LEFT JOIN (SELECT * FROM MPA) as m ON f.MPA_ID = m.MPA_ID",
                this::mapRowToFilms);
    }

    @Override
    public Optional<Film> getFilm(Integer id) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(
                "select f.FILM_ID, f.FILM_NAME, f.DESCRIPTION, f.RELEASE_DATE, f.DURATION, " +
                        "f.MPA_ID, m.MPA_NAME, g.FILM_GENRES " +
                        "from FILMS as f " +
                        "LEFT JOIN (SELECT FILM_ID, string_agg(GENRE_ID, ',') AS FILM_GENRES " +
                        "FROM FILM_GENRES " +
                        "GROUP BY FILM_ID) AS g " +
                        "ON g.FILM_ID = f.FILM_ID " +
                        "LEFT JOIN (SELECT * FROM MPA) as m ON f.MPA_ID = m.MPA_ID " +
                        "where f.FILM_ID = ?",
                this::mapRowToFilms,
                id));
    }

    @Override
    public Film create(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("FILMS")
                .usingGeneratedKeyColumns("FILM_ID");
        film.setId(simpleJdbcInsert.executeAndReturnKey(film.toMap()).intValue());
        if (!film.getGenres().isEmpty()) {
            updateGenres(film.getId(), film.getGenres());
        }
        return film;
    }

    @Override
    public void update(Film film) {
        String sql = "update FILMS set " +
                "FILM_NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ?, MPA_ID = ? " +
                "where FILM_ID = ?";
        jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
        List<Genre> filmGenres = film.getGenres();
        if (!filmGenres.isEmpty()) {
            updateGenres(film.getId(), filmGenres);
        }
    }

    private void updateGenres(Integer filmId, List<Genre> filmGenres) {
        String sqlQuery = "MERGE INTO FILM_GENRES (FILM_ID, GENRE_ID) VALUES (?, ?)";
        jdbcTemplate.batchUpdate(sqlQuery,
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        Genre genre = filmGenres.get(0);
                        ps.setInt(1, filmId);
                        ps.setInt(2, genre.getId());
                    }

                    @Override
                    public int getBatchSize() {
                        return filmGenres.size();
                    }
                });
    }

    @Override
    public void addLike(Integer filmId, Long userId) {
        String sqlQuery = "update FILM_LIKES set " +
                "USER_ID = ? " +
                "where FILM_ID = ?";
        jdbcTemplate.update(sqlQuery, userId, filmId);
    }

    @Override
    public void removeLike(Integer filmId, Long userId) {
        String sqlQuery = "delete from FILM_LIKES " +
                "where FILM_ID = ? and USER_ID = ?";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    @Override
    public List<Film> getTopFilms(Integer count) {
        String sqlQuery = "select f.FILM_ID, f.FILM_NAME, f.DESCRIPTION, f.RELEASE_DATE, f.DURATION, " +
                "f.MPA_ID, m.MPA_NAME, g.FILM_GENRES, " +
                "from FILM_LIKES as fl " +
                "LEFT JOIN FILMS as f ON fl.FILM_ID = f.FILM_ID " +
                "LEFT JOIN (SELECT FILM_ID, string_agg(GENRE_ID, ',') AS FILM_GENRES " +
                "FROM FILM_GENRES " +
                "GROUP BY FILM_ID) AS g " +
                "ON g.FILM_ID = fl.FILM_ID " +
                "LEFT JOIN (SELECT * FROM MPA) as m ON f.MPA_ID = m.MPA_ID " +
                "GROUP BY fl.FILM_ID " +
                "ORDER BY count (fl.USER_ID) DESC " +
                "LIMIT " + count;
        return jdbcTemplate.query(
                sqlQuery,
                this::mapRowToFilms);
    }

    private Film mapRowToFilms(ResultSet rs, int rowNum) throws SQLException {
        List<Genre> filmGenres = new ArrayList<>();
        if (rs.getString("FILM_GENRES") != null) {
            filmGenres = Arrays.stream(rs.getString("FILM_GENRES").split(","))
                    .map(Integer::parseInt)
                    .map(Genre::new)
                    .collect(Collectors.toList());
        }

        return Film.builder()
                .id(rs.getInt("FILM_ID"))
                .name(rs.getString("FILM_NAME"))
                .description(rs.getString("DESCRIPTION"))
                .releaseDate(rs.getDate("RELEASE_DATE").toLocalDate())
                .duration(rs.getInt("DURATION"))
                .mpa(new MpaRate(rs.getInt("MPA_ID"), rs.getString("MPA_NAME")))
                .genres(filmGenres)
                .build();
    }
}
