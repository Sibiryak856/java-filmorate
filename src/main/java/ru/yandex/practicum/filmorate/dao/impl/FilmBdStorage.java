package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
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
        return jdbcTemplate.query("WITH " +
                        "GENRE_CONCAT AS (\n" +
                        "SELECT GENRE_ID, CONCAT_WS(',', GENRE_ID, GENRE_NAME) AS CONC_GENRE\n" +
                        "FROM GENRES\n" +
                        "),\n" +
                        "FILM_AGG_GENRES AS (\n" +
                        "SELECT FILM_ID, string_agg(gc.CONC_GENRE, ';') AS FILM_GENRES\n" +
                        "FROM FILM_GENRES fg \n" +
                        "LEFT JOIN GENRE_CONCAT as gc\n" +
                        "ON fg.GENRE_ID = gc.GENRE_ID\n" +
                        "GROUP BY fg.FILM_ID\n" +
                        ")\n" +
                        "SELECT f.FILM_ID, f.FILM_NAME, f.DESCRIPTION, f.RELEASE_DATE, f.DURATION, f.MPA_ID, m.MPA_NAME, fg.FILM_GENRES \n" +
                        "FROM FILMS AS f \n" +
                        "LEFT JOIN FILM_AGG_GENRES fg ON fg.FILM_ID = f.FILM_ID\n" +
                        "LEFT JOIN MPA AS m ON f.MPA_ID = m.MPA_ID",
                this::mapRowToFilms);
    }

    @Override
    public Optional<Film> getFilm(Integer id) {
        Film film;
        try {
            film = jdbcTemplate.queryForObject(
                    "WITH " +
                            "GENRE_CONCAT AS (\n" +
                            "SELECT GENRE_ID, CONCAT_WS(',', GENRE_ID, GENRE_NAME) AS CONC_GENRE\n" +
                            "FROM GENRES\n" +
                            "),\n" +
                            "FILM_AGG_GENRES AS (\n" +
                            "SELECT FILM_ID, string_agg(gc.CONC_GENRE, ';') AS FILM_GENRES\n" +
                            "FROM FILM_GENRES fg \n" +
                            "LEFT JOIN GENRE_CONCAT as gc\n" +
                            "ON fg.GENRE_ID = gc.GENRE_ID\n" +
                            "GROUP BY fg.FILM_ID\n" +
                            ")\n" +
                            "SELECT f.FILM_ID, f.FILM_NAME, f.DESCRIPTION, f.RELEASE_DATE, f.DURATION, f.MPA_ID, m.MPA_NAME, fg.FILM_GENRES \n" +
                            "FROM FILMS AS f \n" +
                            "LEFT JOIN FILM_AGG_GENRES fg ON fg.FILM_ID = f.FILM_ID\n" +
                            "LEFT JOIN MPA AS m ON f.MPA_ID = m.MPA_ID\n" +
                            "WHERE f.FILM_ID = ?",
                    this::mapRowToFilms,
                    id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException(String.format("Film id=%d not found", id));
        }
        return Optional.ofNullable(film);
    }

    @Override
    public Film create(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("FILMS")
                .usingGeneratedKeyColumns("FILM_ID");
        int id = simpleJdbcInsert.executeAndReturnKey(film.toMap()).intValue();
        film.setId(id);
        if (film.getGenres() != null) {
            updateGenres(film.getId(), film.getGenres());
        }
        return film;
    }

    @Override
    public void update(Film film) {
        jdbcTemplate.update(
                "update FILMS set FILM_NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ?, MPA_ID = ?\n" +
                        "WHERE FILM_ID = ?",
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());

        if (film.getGenres() != null) {
            clearFilmGenres(film.getId());
            updateGenres(film.getId(), film.getGenres());
        }
    }

    private void clearFilmGenres(Integer id) {
        jdbcTemplate.update("DELETE FROM FILM_GENRES WHERE FILM_ID = ?", id);
    }

    private void updateGenres(Integer filmId, List<Genre> filmGenres) {
        if (!filmGenres.isEmpty()) {
            jdbcTemplate.batchUpdate("MERGE INTO FILM_GENRES (FILM_ID, GENRE_ID) VALUES (?, ?)",
                    new BatchPreparedStatementSetter() {
                        @Override
                        public void setValues(PreparedStatement ps, int i) throws SQLException {
                            Genre genre = filmGenres.get(i);
                            ps.setInt(1, filmId);
                            ps.setInt(2, genre.getId());
                        }

                        @Override
                        public int getBatchSize() {
                            return filmGenres.size();
                        }
                    });
        }
    }

    @Override
    public void addLike(Integer filmId, Long userId) {
        String sqlQuery = "MERGE INTO FILM_LIKES (FILM_ID, USER_ID) VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    @Override
    public void removeLike(Integer filmId, Long userId) {
        String sqlQuery = "DELETE FROM FILM_LIKES\n" +
                "WHERE FILM_ID = ? AND USER_ID = ?";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    @Override
    public List<Film> getTopFilms(Integer count) {
        return jdbcTemplate.query(
                String.format("WITH GENRE_CONCAT AS (\n" +
                        "SELECT GENRE_ID, CONCAT_WS(',', GENRE_ID, GENRE_NAME) AS CONC_GENRE\n" +
                        "FROM GENRES\n" +
                        "),\n" +
                        "FILM_AGG_GENRES AS (\n" +
                        "SELECT FILM_ID, string_agg(gc.CONC_GENRE, ';') AS FILM_GENRES\n" +
                        "FROM FILM_GENRES fg \n" +
                        "LEFT JOIN GENRE_CONCAT as gc\n" +
                        "ON fg.GENRE_ID = gc.GENRE_ID\n" +
                        "GROUP BY fg.FILM_ID\n" +
                        "),\n" +
                        "FILM_LIKES_COUNT AS (\n" +
                        "SELECT fl.FILM_ID, count(fl.USER_ID) AS QTY_LIKES\n" +
                        "FROM FILM_LIKES fl\n" +
                        "GROUP BY fl.FILM_ID\n" +
                        ")\n" +
                        "SELECT f.*, f.MPA_ID, m.MPA_NAME, fg.FILM_GENRES, flc.QTY_LIKES\n" +
                        "FROM FILMS AS f\n" +
                        "LEFT JOIN FILM_AGG_GENRES fg ON fg.FILM_ID = f.FILM_ID\n" +
                        "LEFT JOIN MPA as m ON f.MPA_ID = m.MPA_ID\n" +
                        "LEFT JOIN FILM_LIKES_COUNT AS flc ON flc.FILM_ID = f.FILM_ID\n" +
                        "GROUP BY f.FILM_ID\n" +
                        "ORDER BY flc.QTY_LIKES DESC\n" +
                        "LIMIT %d", + count),
                this::mapRowToFilms);
    }

    private Film mapRowToFilms(ResultSet rs, int rowNum) throws SQLException {
        return Film.builder()
                    .id(rs.getInt("FILM_ID"))
                    .name(rs.getString("FILM_NAME"))
                    .description(rs.getString("DESCRIPTION"))
                    .releaseDate(rs.getDate("RELEASE_DATE").toLocalDate())
                    .duration(rs.getInt("DURATION"))
                    .mpa(new MpaRate(rs.getInt("MPA_ID"), rs.getString("MPA_NAME")))
                    .genres(getFilmGenres(rs.getString("FILM_GENRES")))
                    .build();
    }

    private List<Genre> getFilmGenres(String filmGenres) {
        if (filmGenres == null) {
            return new ArrayList<>();
        }

        return Arrays.asList(filmGenres.split(";")).stream()
                .map(genre -> {
                    String[] genreParams = genre.split(",");
                    int id = Integer.parseInt(genreParams[0]);
                    String name = genreParams[1];
                    return new Genre(id, name);
                })
                .collect(Collectors.toList());
    }

}
