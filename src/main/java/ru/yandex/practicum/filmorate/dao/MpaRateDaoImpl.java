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

@Component("mpaRateDao")
@RequiredArgsConstructor
public class MpaRateDaoImpl implements MpaRateDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<MpaRating> getAll() {
        return jdbcTemplate.query("select * " +
                        "from MPA",
                this::mapRowToMpaRating);
    }

    @Override
    public Optional<MpaRating> getMpaRate(Integer id) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(
                "select * from MPA where MPA_ID = ?",
                this::mapRowToMpaRating,
                id));
    }

    private MpaRating mapRowToMpaRating(ResultSet resultSet, int i) throws SQLException {
        return new MpaRating(resultSet.getInt("MPA_ID"), resultSet.getString("MPA_NAME"));
    }
}
