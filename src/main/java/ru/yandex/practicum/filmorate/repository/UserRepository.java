package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.mapper.UserMapper;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Component
@RequiredArgsConstructor
@Primary
@Qualifier("userRepository")
public class UserRepository implements UserStorage {
    private final JdbcTemplate jdbcTemplate;
    private final UserMapper userMapper;
    private static final String GET_QUERY = "SELECT * FROM users";
    private static final String CREATE_QUERY = "INSERT INTO users (name, login, email, birthday) VALUES (?, ?, ?, ?)";
    private static final String GET_ID_QUERY = "SELECT * FROM users WHERE id = ?";
    private static final String UPDATE_QUERY = "UPDATE users SET name = ?, login = ?, email = ?, birthday = ? WHERE id = ?";
    private static final String DELETE_QUERY = "DELETE FROM users WHERE id = ?";

    @Override
    public List<User> getAll() {

        return jdbcTemplate.query(GET_QUERY, userMapper);
    }

    @Override
    public User create(User user) {

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(CREATE_QUERY, Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, user.getName());
            ps.setObject(2, user.getLogin());
            ps.setObject(3, user.getEmail());
            ps.setObject(4, user.getBirthday());
            return ps;
        }, keyHolder);

        Long id = keyHolder.getKeyAs(Long.class);


        if (id != null) {
            user.setId(id);
        } else {
            throw new RuntimeException("Не удалось сохранить данные");
        }
        return user;
    }

    @Override
    public User getById(Long id) {
        try {
            return jdbcTemplate.queryForObject(GET_ID_QUERY, userMapper, id);
        } catch (EmptyResultDataAccessException e) {
            throw new UserNotFoundException("Пользователь с id " + id + " не найден");
        }
    }

    @Override
    public User update(User user) {
        try {
            jdbcTemplate.queryForObject(GET_ID_QUERY, userMapper, user.getId());
        } catch (EmptyResultDataAccessException e) {
            throw new UserNotFoundException("Пользователь с id " + user.getId() + " не найден для обновления");
        }

        jdbcTemplate.update(UPDATE_QUERY,
                user.getName(),
                user.getLogin(),
                user.getEmail(),
                user.getBirthday(),
                user.getId());
        return user;
    }

    @Override
    public void deleteById(Long id) {
        int rowsUpdated = jdbcTemplate.update(DELETE_QUERY, id);
        if (rowsUpdated == 0) {
            throw new RuntimeException("Не удалось обновить данные");
        }
    }


}


