package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Component
@RequiredArgsConstructor
@Primary
@Qualifier("filmRepository")
public class FilmRepository implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final FilmMapper filmMapper;

    private static final String GET_ALL = "SELECT f.*, m.description AS mpa_name" +
            " FROM film f" +
            " LEFT JOIN mpa m ON f.mpa_id = m.mpa_id";
    private static final String GET_BY_ID = "SELECT f.*, m.description AS mpa_name" +
            " FROM film f" +
            " LEFT JOIN mpa m ON f.mpa_id = m.mpa_id" +
            " WHERE film_id = ?";
    private static final String INSERT_FILM = "INSERT INTO film (name, description, release_date, duration, mpa_id) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_FILM = "UPDATE film SET name = ?, description = ?, release_date = ?, duration = ?, mpa_id = ? WHERE film_id = ?";
    private static final String DELETE_FILM = "DELETE FROM film WHERE film_id = ?";

    @Override
    public List<Film> getAll() {
        return jdbcTemplate.query(GET_ALL, filmMapper);
    }

    @Override
    public Film getById(Long id) {
        try {
            return jdbcTemplate.queryForObject(GET_BY_ID, filmMapper, id);
        } catch (EmptyResultDataAccessException e) {
            throw new FilmNotFoundException("Фильм с id " + id + " не найден");
        }
    }

    @Override
    public Film create(Film film) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(INSERT_FILM, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setDate(3, java.sql.Date.valueOf(film.getReleaseDate()));
            ps.setInt(4, film.getDuration());
            ps.setInt(5, film.getMpa().getId());
            return ps;
        }, keyHolder);

        Long id = keyHolder.getKeyAs(Long.class);
        if (id == null) {
            throw new RuntimeException("Не удалось сохранить фильм");
        }

        film.setId(id);
        return film;
    }

    @Override
    public Film update(Film film) {
        int updated = jdbcTemplate.update(UPDATE_FILM,
                film.getName(),
                film.getDescription(),
                java.sql.Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());

        if (updated == 0) {
            throw new FilmNotFoundException("Фильм с id " + film.getId() + " не найден для обновления");
        }

        return film;
    }

    @Override
    public void deleteById(Long id) {
        int deleted = jdbcTemplate.update(DELETE_FILM, id);
        if (deleted == 0) {
            throw new FilmNotFoundException("Фильм с id " + id + " не найден для удаления");
        }
    }
}
