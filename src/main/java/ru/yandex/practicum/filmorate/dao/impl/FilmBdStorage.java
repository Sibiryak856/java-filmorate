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
                        "GROUP BY fg.FILM_ID)\n" +
                        "SELECT f.FILM_ID, f.FILM_NAME, f.DESCRIPTION, f.RELEASE_DATE, f.DURATION, f.MPA_ID, m.MPA_NAME, fg.FILM_GENRES \n" +
                        "FROM FILMS AS f \n" +
                        "LEFT JOIN FILM_AGG_GENRES fg ON fg.FILM_ID = f.FILM_ID\n" +
                        "LEFT JOIN MPA AS m ON f.MPA_ID = m.MPA_ID",
                this::mapRowToFilms);
    }

    @Override
    public Optional<Film> getFilm(Integer id) {
        Film film = null;
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
                            "GROUP BY fg.FILM_ID)\n" +
                            "SELECT f.FILM_ID, f.FILM_NAME, f.DESCRIPTION, f.RELEASE_DATE, f.DURATION, f.MPA_ID, m.MPA_NAME, fg.FILM_GENRES \n" +
                            "FROM FILMS AS f \n" +
                            "LEFT JOIN FILM_AGG_GENRES fg ON fg.FILM_ID = f.FILM_ID\n" +
                            "LEFT JOIN MPA AS m ON f.MPA_ID = m.MPA_ID\n" +
                            "WHERE f.FILM_ID = ?",
                    /*"SELECT " +
                            "f.FILM_ID, " +
                            "f.FILM_NAME, " +
                            "f.DESCRIPTION, " +
                            "f.RELEASE_DATE, " +
                            "f.DURATION, " +
                            "f.MPA_ID, " +
                            "m.MPA_NAME, " +
                            "g.FILM_GENRES " +
                            "FROM FILMS AS f " +
                            "LEFT JOIN (SELECT FILM_ID, string_agg(string_agg(GENRE_ID, ',') AS FILM_GENRES\n" +
                                "FROM FILM_GENRES\n" +
                                "GROUP BY FILM_ID) AS g\n" +
                            "ON g.FILM_ID = f.FILM_ID\n" +
                            "LEFT JOIN MPA AS m ON f.MPA_ID = m.MPA_ID\n" +
                            "WHERE f.FILM_ID = ?",*/
                    this::mapRowToFilms,
                    id);
        } catch (EmptyResultDataAccessException e) {
            new NotFoundException(String.format("Film id=%d not found", id));
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
        if (!film.getGenres().isEmpty()) {
            updateGenres(film.getId(), film.getGenres());
        }
        return film;
    }

    @Override
    public void update(Film film) {
        String sql = "update FILMS set " +
                "FILM_NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ?, MPA_ID = ? " +
                "WHERE FILM_ID = ?";
        jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());

        updateGenres(film.getId(),  film.getGenres());
    }

    private void updateGenres(Integer filmId, List<Genre> filmGenres) {
        String sqlQuery = "MERGE INTO FILM_GENRES (FILM_ID, GENRE_ID)\n" +
                "VALUES (?, ?)";
        if (filmGenres == null || filmGenres.isEmpty()) {
            sqlQuery = "DELETE " +
                    "FROM FILM_GENRES " +
                    "WHERE FILM_ID = ? AND GENRE_ID = ?";
        }
        jdbcTemplate.batchUpdate(sqlQuery,
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

    @Override
    public void addLike(Integer filmId, Long userId) {
        String sqlQuery = "MERGE INTO FILM_LIKES (FILM_ID, USER_ID) " +
                "VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    @Override
    public void removeLike(Integer filmId, Long userId) {
        String sqlQuery = "DELETE " +
                "FROM FILM_LIKES " +
                "WHERE FILM_ID = ? AND USER_ID = ?";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    @Override
    public List<Film> getTopFilms(Integer count) {
        String sqlQuery = "SELECT " +
                "f.FILM_ID, " +
                "f.FILM_NAME, " +
                "f.DESCRIPTION, " +
                "f.RELEASE_DATE, " +
                "f.DURATION, " +
                "f.MPA_ID, " +
                "m.MPA_NAME, " +
                "g.FILM_GENRES\n" +
                "FROM FILM_LIKES as fl\n" +
                "LEFT JOIN FILMS AS f ON fl.FILM_ID = f.FILM_ID " +
                "LEFT JOIN (SELECT FILM_ID, string_agg(GENRE_ID, ',') AS FILM_GENRES\n" +
                    "FROM FILM_GENRES\n" +
                    "GROUP BY FILM_ID) AS g\n" +
                "ON g.FILM_ID = fl.FILM_ID\n" +
                "LEFT JOIN MPA as m\n" +
                "ON f.MPA_ID = m.MPA_ID\n" +
                "GROUP BY fl.FILM_ID\n" +
                "ORDER BY count(fl.USER_ID) DESC\n" +
                "LIMIT " + count;
        return jdbcTemplate.query(
                sqlQuery,
                this::mapRowToFilms);
    }

    private Film mapRowToFilms(ResultSet rs, int rowNum) throws SQLException {
        /*List<Genre> filmGenres = new ArrayList<>();
        if (rs.getString("FILM_GENRES") != null) {
            filmGenres = Arrays.asList(rs.getString("FILM_GENRES").split(","))
                    .stream()
                    .map(Integer::parseInt)
                    .map(Genre::new)
                    .collect(Collectors.toList());
        }
        MpaRate mpaRate = new MpaRate(rs.getInt("MPA_ID"), rs.getString("MPA_NAME"));*/
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
        /*List<Genre> list = new ArrayList<>();
        String[] genres = filmGenres.split(";");
        for (int i = 0; i < genres.length; i++) {
            String[] genreParam = genres[i].split(",");
            Genre genre = new Genre(Integer.parseInt(genreParam[0]), genreParam[1]);
            list.add(genre);
        }*/

        return Arrays.asList(filmGenres.split(";")).stream()
                .map(genre -> {
                    String[] genreParams = genre.split(",");
                    int id = Integer.parseInt(genreParams[0]);
                    String name = genreParams[1];
                    return new Genre(id, name);
                })
                .collect(Collectors.toList());
        /*Arrays.asList(filmGenres.split(","))
                .stream()
                .map(Integer::parseInt)
                .map(Genre::new)
                .collect(Collectors.toList());*/
    }
}
