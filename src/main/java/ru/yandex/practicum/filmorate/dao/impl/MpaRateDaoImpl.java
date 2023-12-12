package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.MpaRateDao;
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
        return jdbcTemplate.query("select * " +
                        "from MPA",
                this::mapRowToMpaRating);
    }

    @Override
    public Optional<MpaRate> getMpa(Integer id) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(
                    "select * from MPA where MPA_ID = ?",
                    this::mapRowToMpaRating,
                    id));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    private MpaRate mapRowToMpaRating(ResultSet resultSet, int i) throws SQLException {
        try {
            return new MpaRate(resultSet.getInt("MPA_ID"), resultSet.getString("MPA_NAME"));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
