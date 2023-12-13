package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.MpaRateDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.MpaRate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MpaRateDaoImpl implements MpaRateDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<MpaRate> getAll() {
        return jdbcTemplate.query("SELECT * FROM MPA",
                this::mapRowToMpaRating);
    }

    @Override
    public Optional<MpaRate> getMpa(Integer id) {
        MpaRate mpa;
        try {
            mpa = jdbcTemplate.queryForObject(
                    "SELECT * \n" +
                            "FROM MPA\n" +
                            "WHERE MPA_ID = ?",
                    this::mapRowToMpaRating,
                    id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException(String.format("MPA rate id=%d not found", id));
        }
        return Optional.ofNullable(mpa);
    }

    private MpaRate mapRowToMpaRating(ResultSet resultSet, int i) throws SQLException {
        return new MpaRate(resultSet.getInt("MPA_ID"), resultSet.getString("MPA_NAME"));

    }

}
