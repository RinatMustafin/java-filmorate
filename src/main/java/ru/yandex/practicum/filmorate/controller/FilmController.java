package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.InvalidBirthdayException;
import ru.yandex.practicum.filmorate.exception.InvalidReleaseDateException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private Map<Long, Film> idToFilm = new HashMap<>();
    private Long idCounter = 1l;

    @GetMapping
    public List<Film> getAll() {
        log.info("Получен HTTP-запрос на получение всех фильмов");
        return new ArrayList<>(idToFilm.values());
    }

    @PostMapping
    public Film create(@RequestBody @Valid Film film) {
        log.info("Получен HTTP-запрос на добавление фильма: {}", film);
        validateDate(film);
        film.setId(idCounter++);
        idToFilm.put(film.getId(), film);
        log.info("Успешно обработан HTTP-запрос на добавление фильма: {}", film);
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


    @PutMapping
    public Film update(@RequestBody Film film) {
        log.info("Получен HTTP-запрос на обновление фильма: {}", film);
        Long id = film.getId();
        if (!idToFilm.containsKey(id)) {
            String errorMessage = String.format("Фильм с id %d не найден", id);
            log.error(errorMessage);
            throw new FilmNotFoundException(errorMessage);
        }
        idToFilm.put(film.getId(), film);
        log.info("Успешно обработан HTTP-запрос на обновление фильма: {}", film);
        return film;
    }
}
