package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component
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
        return Optional.ofNullable(jdbcTemplate.queryForObject(
                "select * from GENRE where GENRE_ID = ?",
                this::mapRowToGenre,
                id));
    }

    private Genre mapRowToGenre(ResultSet resultSet, int i) throws SQLException {
        return new Genre(resultSet.getInt("GENRE_ID"),
                resultSet.getString("GENRE_NAME"));
    }
}
