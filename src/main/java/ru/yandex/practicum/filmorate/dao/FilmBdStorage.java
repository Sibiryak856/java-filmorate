package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component("filmDbStorage")
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
        String sqlQuery = "insert into FILMS(FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA_ID) " +
                "values (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"FILM_ID"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
            stmt.setInt(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        film.setId(keyHolder.getKey().intValue());
        MpaRating mpa = new MpaRating(film.getMpa().getId(), Mpa.parse(film.getMpa().getId()).getName());
        film.setMpa(mpa);
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
            updateGenre(film.getId(), filmGenres);
        }
    }

    private void updateGenre(Integer filmId, List<Genre> filmGenres) {
        String sqlQuery = "update FILM_GENRES set " +
                "GENRE_ID = ? " +
                "where FILM_ID = ?";
        filmGenres.forEach(genre -> jdbcTemplate.update(sqlQuery, genre.getId(), filmId));
    }

    @Override
    public void addLike(Integer filmId, Long userId) {
        String sqlQuery = "update FILM_LIKES set " +
                "USER_ID = ? " +
                "where FILM_ID = ?";
        jdbcTemplate.update(sqlQuery, userId, filmId); // return???
    }

    @Override
    public void removeLike(Integer filmId, Long userId) {
        String sqlQuery = "delete from FILM_LIKES " +
                "where FILM_ID = ? and USER_ID = ?";
        jdbcTemplate.update(sqlQuery, filmId, userId); // return???
    }

    @Override
    public List<Film> getTopFilms(Integer count) {
        String sqlQuery = "select f.FILM_ID, f.FILM_NAME, f.DESCRIPTION, f.RELEASE_DATE, f.DURATION, f.MPA_ID, " +
                "m.MPA_NAME, g.FILM_GENRES, " +
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
        /*if (!Mpa.checkMpaID(rs.getInt("MPA_ID"))) {
            throw new NotFoundException("Жанр не найден")
        }*/
        List<Genre> filmGenres = new ArrayList<>();
        // проверка на соответствие enum в service => create
        if (rs.getString("FILM_GENRES") != null) {
            filmGenres = Arrays.asList(rs.getString("FILM_GENRES").split(","))
                    .stream()
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
                .mpa(new MpaRating(rs.getInt("MPA_ID"), rs.getString("MPA_NAME")))
                .genres(filmGenres)
                .build();
    }
}
