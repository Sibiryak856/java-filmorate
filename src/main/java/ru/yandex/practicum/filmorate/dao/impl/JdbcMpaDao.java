package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcMpaDao implements MpaDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public List<Mpa> getAll() {
        return jdbcTemplate.query("SELECT * FROM MPA",
                this::mapRowToMpaRating);
    }

    @Override
    public Optional<Mpa> getMpa(Integer id) {
        Mpa mpa = jdbcTemplate.query(
                "SELECT * \n" +
                        "FROM MPA\n" +
                        "WHERE MPA_ID = :id",
                new MapSqlParameterSource().addValue("id", id),
                rs -> {
                    Mpa res = null;
                    if (rs.next()) {
                        res = new Mpa(rs.getInt("MPA_ID"), rs.getString("MPA_NAME"));
                    }
                    return res;
                });
        return Optional.ofNullable(mpa);
    }

    private Mpa mapRowToMpaRating(ResultSet resultSet, int i) throws SQLException {
        return new Mpa(resultSet.getInt("MPA_ID"), resultSet.getString("MPA_NAME"));
    }

}
