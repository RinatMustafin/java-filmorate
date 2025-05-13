package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.mapper.MpaMapper;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MpaRepository {

    private final JdbcTemplate jdbcTemplate;
    private final MpaMapper mpaMapper;
    private String GET_BY_ID_QUERY = "SELECT * FROM mpa WHERE mpa_id = ?";
    private String GET_ALL_QUERY = "SELECT * FROM mpa ORDER BY mpa_id";

    public Mpa getById(int id) {
        try {
            return jdbcTemplate.queryForObject(GET_BY_ID_QUERY, mpaMapper, id);
        } catch (EmptyResultDataAccessException e) {
            throw new MpaNotFoundException("MPA с id " + id + " не найден.");
        }
    }

    public List<Mpa> getAll() {
        return jdbcTemplate.query(GET_ALL_QUERY, mpaMapper);
    }
}
