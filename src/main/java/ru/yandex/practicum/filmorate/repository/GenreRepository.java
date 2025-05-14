package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.mapper.GenreMapper;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class GenreRepository {
    private final JdbcTemplate jdbcTemplate;
    private final GenreMapper genreMapper;

    public List<Genre> findAll() {
        String sql = "SELECT * FROM genre";
        return jdbcTemplate.query(sql, genreMapper);
    }

    public Optional<Genre> findById(int id) {
        String sql = "SELECT * FROM genre WHERE genre_id = ?";
        List<Genre> genres = jdbcTemplate.query(sql, genreMapper, id);
        return genres.stream().findFirst();
    }

    public List<Genre> findGenresByFilmId(Long filmId) {
        String sql = """
                SELECT g.genre_id, g.description
                FROM film_genre fg
                JOIN genre g ON fg.genre_id = g.genre_id
                WHERE fg.film_id = ?
                ORDER BY g.genre_id
                """;
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new Genre(rs.getInt("genre_id"), rs.getString("description")), filmId);
    }

    public void saveGenresForFilm(Long filmId, List<Genre> genres) {

        Set<Integer> uniqueGenreIds = new HashSet<>();
        for (Genre genre : genres) {
            if (uniqueGenreIds.add(genre.getId())) {
                jdbcTemplate.update(
                        "INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?)",
                        filmId, genre.getId()
                );
            }
        }
    }
}
