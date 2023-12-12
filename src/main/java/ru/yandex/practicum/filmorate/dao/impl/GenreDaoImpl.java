package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class GenreDaoImpl implements GenreDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Genre> getAll() {
        return jdbcTemplate.query("select * " +
                        "from GENRES",
                this::mapRowToGenre);
    }

    @Override
    public Optional<Genre> getGenre(Integer id) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(
                    "select * from GENRES where GENRE_ID = ?",
                    this::mapRowToGenre,
                    id));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    private Genre mapRowToGenre(ResultSet resultSet, int i) throws SQLException {
        try {
            return new Genre(resultSet.getInt("GENRE_ID"),
                    resultSet.getString("GENRE_NAME"));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
