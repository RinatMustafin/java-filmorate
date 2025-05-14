package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class LikeRepository {
    private final JdbcTemplate jdbcTemplate;

    public void addLike(Long filmId, Long userId) {
        String sql = "INSERT INTO likes (film_id, user_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, filmId, userId);
    }

    public void removeLike(Long filmId, Long userId) {
        String sql = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(sql, filmId, userId);
    }

    public int getLikeCount(Long filmId) {
        String sql = "SELECT COUNT(*) FROM likes WHERE film_id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, filmId);
    }

    public boolean isLikedByUser(Long filmId, Long userId) {
        String sql = "SELECT COUNT(*) FROM likes WHERE film_id = ? AND user_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, filmId, userId);
        return count != null && count > 0;
    }

    public List<Long> getTopFilmIds(int count) {
        String sql = """
                SELECT f.film_id
                FROM film f
                LEFT JOIN likes l ON f.film_id = l.film_id
                GROUP BY f.film_id
                ORDER BY COUNT(l.user_id) DESC
                LIMIT ?
                """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getLong("film_id"), count);
    }
}
