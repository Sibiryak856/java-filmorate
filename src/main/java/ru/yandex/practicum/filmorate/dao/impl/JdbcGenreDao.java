package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcGenreDao implements GenreDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public List<Genre> getAll() {
        return jdbcTemplate.query("SELECT * FROM GENRES",
                this::mapRowToGenre);
    }

    @Override
    public Optional<Genre> getGenre(Integer id) {
        return jdbcTemplate.query(
                "SELECT * FROM GENRES\n" +
                        "WHERE GENRE_ID = :id",
                new MapSqlParameterSource().addValue("id", id),
                rs -> {
                    if (!rs.next()) {
                        return Optional.empty();
                    }
                    return Optional.of(new Genre(
                            rs.getInt("GENRE_ID"),
                            rs.getString("GENRE_NAME")));
                });
    }

    private Genre mapRowToGenre(ResultSet resultSet, int i) throws SQLException {
        return new Genre(resultSet.getInt("GENRE_ID"),
                resultSet.getString("GENRE_NAME"));
    }

}
