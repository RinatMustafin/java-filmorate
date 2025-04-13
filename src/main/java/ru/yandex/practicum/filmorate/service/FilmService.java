package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public List<Film> getAll() {
        List<Film> films = filmStorage.getAll();
        return films;
    }

    public Film getById(Long id) {
        Film filmStorageById = filmStorage.getById(id);
        return filmStorageById;
    }

    public Film create(Film film) {
        Film createdFilm = filmStorage.create(film);
        return createdFilm;
    }

    public Film update(Film film) {
        Film updateFilm = filmStorage.update(film);
        return updateFilm;
    }

    public void deleteById(Long id) {
        filmStorage.deleteById(id);
    }

    public void putLike(Long filmId, Long userId) {
        Film filmById = filmStorage.getById(filmId);
        User user = userStorage.getById(userId);
        if (filmById == null) {
            String errorMessage = String.format("Фильм id %d не найден", filmId);
            log.error(errorMessage);
            throw new FilmNotFoundException(errorMessage);
        }
        if (user == null) {
            String errorMessage = String.format("Пользователь id %d не найден", userId);
            log.error(errorMessage);
            throw new UserNotFoundException(errorMessage);
        }
        filmById.addLike(userId);
        filmStorage.update(filmById);
    }

    public void removeLike(Long filmId, Long userId) {
        Film filmById = filmStorage.getById(filmId);
        User user = userStorage.getById(userId);
        if (filmById == null) {
            String errorMessage = String.format("Фильм id %d не найден", filmId);
            log.error(errorMessage);
            throw new FilmNotFoundException(errorMessage);
        }
        if (user == null) {
            String errorMessage = String.format("Пользователь id %d не найден", userId);
            log.error(errorMessage);
            throw new UserNotFoundException(errorMessage);
        }
        filmById.removeLike(userId);
        filmStorage.update(filmById);
    }

    public List<Film> getTopPopularFilms(int count) {
        return filmStorage.getAll().stream()
                .sorted(Comparator.comparingInt(film -> -film.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }
}
