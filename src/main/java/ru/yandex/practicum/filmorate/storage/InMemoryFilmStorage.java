package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.InvalidReleaseDateException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service
@Qualifier("inMemoryFilmStorage")
public class InMemoryFilmStorage implements FilmStorage {

    private Map<Long, Film> idToFilm = new HashMap<>();
    private Long idCounter = 1L;

    @Override
    public List<Film> getAll() {
        return new ArrayList<>(idToFilm.values());
    }

    @Override
    public Film getById(Long id) {
        return idToFilm.values().stream()
                .filter(film -> Objects.equals(film.getId(), id))
                .findFirst()
                .orElseThrow(
                        () -> {
                            String errorMessage = String.format("Фильм id %d не найден ", id);
                            log.error(errorMessage);
                            throw new FilmNotFoundException(errorMessage);
                        });
    }

    @Override
    public void deleteById(Long id) {
        if (idToFilm.containsKey(id)) {
            idToFilm.remove(id);
        } else {
            String errorMessage = String.format("Фильм id %d не найден", id);
            log.error(errorMessage);
            throw new FilmNotFoundException(errorMessage);
        }
    }

    @Override
    public Film create(Film film) {
        validateDate(film);
        film.setId(idCounter++);
        idToFilm.put(film.getId(), film);
        return film;
    }

    private void validateDate(Film film) {
        LocalDate neededDate = LocalDate.of(1895, 12, 28);
        if (film.getReleaseDate().isBefore(neededDate)) {
            String errorMessage = "Дата релиза не валидна";
            log.error(errorMessage);
            throw new InvalidReleaseDateException(errorMessage);
        }
    }

    @Override
    public Film update(Film film) {
        Long id = film.getId();
        if (!idToFilm.containsKey(id)) {
            String errorMessage = String.format("Фильм с id %d не найден", id);
            log.error(errorMessage);
            throw new FilmNotFoundException(errorMessage);
        }
        idToFilm.put(film.getId(), film);
        return film;
    }
}
