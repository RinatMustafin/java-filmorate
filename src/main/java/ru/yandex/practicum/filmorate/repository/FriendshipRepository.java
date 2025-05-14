package ru.yandex.practicum.filmorate.repository;


import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.mapper.UserMapper;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FriendshipRepository {
    private final JdbcTemplate jdbcTemplate;
    private final UserMapper userMapper;

    private static final String ADD_FRIEND_QUERY = "INSERT INTO friendship (user_id, friend_id) VALUES (?, ?)";
    private static final String DELETE_FRIEND_QUERY = "DELETE FROM friendship WHERE user_id = ? and friend_id = ?";
    private static final String GET_FRIEND_QUERY = "SELECT * FROM users u" +
            " JOIN friendship f ON  u.id = f.friend_id " +
            " WHERE f.user_id = ?";

    private static final String GET_COMMON_FRIEND_QUERY = "SELECT u.* FROM users u " +
            "JOIN friendship f1 ON u.id = f1.friend_id " +
            "JOIN friendship f2 ON u.id = f2.friend_id " +
            "WHERE f1.user_id = ? AND f2.user_id = ?";

    public void addFriend(Long userId, Long friendId) {
        jdbcTemplate.update(
                ADD_FRIEND_QUERY, userId, friendId);
    }

    public void deleteFriend(Long userId, Long friendId) {
        jdbcTemplate.update(
                DELETE_FRIEND_QUERY, userId, friendId);
    }

    public List<User> getFriends(Long userId) {
        return jdbcTemplate.query(GET_FRIEND_QUERY, userMapper, userId);
    }

    public List<User> getCommonFriends(Long userId, Long otherId) {
        return jdbcTemplate.query(
                GET_COMMON_FRIEND_QUERY, userMapper, userId, otherId
        );
    }
}
