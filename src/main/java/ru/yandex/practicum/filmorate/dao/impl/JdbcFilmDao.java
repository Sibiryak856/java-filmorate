package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class JdbcFilmDao implements FilmDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;


    @Override
    public List<Film> getAll() {
        return jdbcTemplate.query("WITH\n" +
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
                        "SELECT f.*, m.MPA_NAME, fg.FILM_GENRES \n" +
                        "FROM FILMS AS f \n" +
                        "LEFT JOIN FILM_AGG_GENRES fg ON fg.FILM_ID = f.FILM_ID\n" +
                        "LEFT JOIN MPA AS m ON f.MPA_ID = m.MPA_ID",
                this::mapRowToFilms);
    }

    @Override
    public Optional<Film> getFilm(Integer id) {
        SqlParameterSource params = new MapSqlParameterSource().addValue("id", id);
        Film film = jdbcTemplate.query(
                "WITH\n" +
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
                        "SELECT f.*, m.MPA_NAME, fg.FILM_GENRES \n" +
                        "FROM FILMS AS f \n" +
                        "LEFT JOIN FILM_AGG_GENRES fg ON fg.FILM_ID = f.FILM_ID\n" +
                        "LEFT JOIN MPA AS m ON f.MPA_ID = m.MPA_ID\n" +
                        "WHERE f.FILM_ID = :id",
                params,
                rs -> {
                    Film extractedFilm = null;
                    if (rs.next()) {
                        extractedFilm = Film.builder()
                                .id(rs.getInt("FILM_ID"))
                                .name(rs.getString("FILM_NAME"))
                                .description(rs.getString("DESCRIPTION"))
                                .releaseDate(rs.getDate("RELEASE_DATE").toLocalDate())
                                .duration(rs.getInt("DURATION"))
                                .mpa(new Mpa(rs.getInt("MPA_ID"), rs.getString("MPA_NAME")))
                                .genres(getFilmGenres(rs.getString("FILM_GENRES")))
                                .build();
                    }
                    return extractedFilm;
                });
        return Optional.ofNullable(film);

    }

    @Override
    public Film create(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
                .withTableName("FILMS")
                .usingColumns("FILM_NAME", "DESCRIPTION", "RELEASE_DATE", "DURATION", "MPA_ID")
                .usingGeneratedKeyColumns("FILM_ID");
        int id = simpleJdbcInsert.executeAndReturnKey(toMap(film)).intValue();
        film.setId(id);
        if (film.getGenres() != null) {
            updateGenres(film.getId(), film.getGenres());
        }
        return film;
    }

    @Override
    public void update(Film film) {
        jdbcTemplate.update(
                "update FILMS SET " +
                        "FILM_NAME = :FILM_NAME, " +
                        "DESCRIPTION = :DESCRIPTION, " +
                        "RELEASE_DATE = :RELEASE_DATE, " +
                        "DURATION = :DURATION, " +
                        "MPA_ID = :MPA_ID\n" +
                        "WHERE FILM_ID = :FILM_ID",
                new MapSqlParameterSource(toMap(film)));

        if (film.getGenres() != null) {
            clearFilmGenres(film.getId());
            updateGenres(film.getId(), film.getGenres());
        }
    }

    private void clearFilmGenres(Integer id) {
        jdbcTemplate.update(
                "DELETE FROM FILM_GENRES WHERE FILM_ID = :id",
                new MapSqlParameterSource().addValue("id", id));
    }

    private void updateGenres(Integer filmId, Set<Genre> filmGenres) {
        if (!filmGenres.isEmpty()) {
            List<Genre> genresList = new ArrayList<>(filmGenres);
            Map<String, Integer>[] valueMaps = new Map[genresList.size()];
            for (int i = 0; i < genresList.size(); i++) {
                Map<String, Integer> map = new HashMap<>();
                map.put("filmId", filmId);
                map.put("genreId", genresList.get(i).getId());
                valueMaps[i] = map;
            }
            SqlParameterSource[] params = SqlParameterSourceUtils.createBatch(valueMaps);

            jdbcTemplate.batchUpdate(
                    "MERGE INTO FILM_GENRES (FILM_ID, GENRE_ID) VALUES (:filmId, :genreId)",
                    params);
        }
    }

    @Override
    public void addLike(Integer filmId, Long userId) {
        jdbcTemplate.update("MERGE INTO FILM_LIKES (FILM_ID, USER_ID)\n VALUES (:filmId, :userId)",
                new MapSqlParameterSource()
                        .addValue("filmId", filmId)
                        .addValue("userId", userId));
    }

    @Override
    public void removeLike(Integer filmId, Long userId) {
        jdbcTemplate.update(
                "DELETE FROM FILM_LIKES\n" +
                        "WHERE FILM_ID = :filmId AND USER_ID = :userId",
                new MapSqlParameterSource()
                        .addValue("filmId", filmId)
                        .addValue("userId", userId));
    }

    @Override
    public List<Film> getTopFilms(Integer count) {
        return jdbcTemplate.query(
               "WITH GENRE_CONCAT AS (\n" +
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
                        "LIMIT :count",
                new MapSqlParameterSource()
                        .addValue("count", count),
                this::mapRowToFilms);
    }

    private Map<String, Object> toMap(Film film) {
        Map<String, Object> values = new HashMap<>();
        values.put("FILM_ID", film.getId());
        values.put("FILM_NAME", film.getName());
        values.put("DESCRIPTION", film.getDescription());
        values.put("RELEASE_DATE", film.getReleaseDate());
        values.put("DURATION", film.getDuration());
        values.put("MPA_ID", film.getMpa().getId());
        return values;
    }

    private Film mapRowToFilms(ResultSet rs, int rowNum) throws SQLException {
        return Film.builder()
                .id(rs.getInt("FILM_ID"))
                .name(rs.getString("FILM_NAME"))
                .description(rs.getString("DESCRIPTION"))
                .releaseDate(rs.getDate("RELEASE_DATE").toLocalDate())
                .duration(rs.getInt("DURATION"))
                .mpa(new Mpa(rs.getInt("MPA_ID"), rs.getString("MPA_NAME")))
                .genres(getFilmGenres(rs.getString("FILM_GENRES")))
                .build();
    }

    private LinkedHashSet<Genre> getFilmGenres(String filmGenres) {
        if (filmGenres == null) {
            return new LinkedHashSet<>();
        }

        return Arrays.asList(filmGenres.split(";")).stream()
                .map(genre -> {
                    String[] genreParams = genre.split(",");
                    int id = Integer.parseInt(genreParams[0]);
                    String name = genreParams[1];
                    return new Genre(id, name);
                }).collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
