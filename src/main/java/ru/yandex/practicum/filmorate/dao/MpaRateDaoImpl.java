package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component("mpaDao")
@RequiredArgsConstructor
public class MpaRateDaoImpl implements MpaRateDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Mpa> getAll() {
        return jdbcTemplate.query("select * " +
                        "from MPA",
                this::mapRowToMpa);
    }

    @Override
    public Optional<MpaRating> getMpaRate(Integer id) {
        /*return Optional.ofNullable(jdbcTemplate.queryForObject(
                "f.FILM_ID, f.FILM_NAME, f.DESCRIPTION, f.RELEASE_DATE, f.DURATION, f.MPA_ID, " +
                        "g.FILM_GENRES " +
                        "from FILMS as f" +
                        "LEFT JOIN (SELECT FILM_ID, array_agg(GENRE_ID) AS FILM_GENRES " +
                        "FROM FILM_GENRES " +
                        "GROUP BY FILM_ID) AS g " +
                        "ON g.FILM_ID = f.FILM_ID " +
                        "where FILM_ID = ?",
                this::mapRowToFilms,
                id));*/
        return null;
    }

    private Mpa mapRowToMpa(ResultSet resultSet, int i) throws SQLException {
        return Mpa.parse(resultSet.getInt("MPA_ID"));
    }
}
