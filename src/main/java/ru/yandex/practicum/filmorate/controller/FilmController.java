package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;

    @GetMapping
    public List<Film> getAll() {
        log.info("Получен HTTP-запрос на получение всех фильмов");
        List<Film> films = filmService.getAll();
        return films;
    }

    @PostMapping
    public Film create(@RequestBody @Valid Film film) {
        log.info("Получен HTTP-запрос на добавление фильма: {}", film);
        Film createdFilm = filmService.create(film);
        log.info("Успешно обработан HTTP-запрос на добавление фильма: {}", film);
        return createdFilm;
    }


    @GetMapping("/{id}")
    public Film getById(@PathVariable Long id) {
        log.info("Получен HTTP-запрос на получение фильма по id: {}", id);
        Film filmById = filmService.getById(id);
        return filmById;
    }


    @PutMapping
    public Film update(@RequestBody Film film) {
        log.info("Получен HTTP-запрос на обновление фильма: {}", film);
        Film updateFilm = filmService.update(film);
        log.info("Успешно обработан HTTP-запрос на обновление фильма: {}", film);
        return updateFilm;
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        filmService.deleteById(id);
    }

    @PutMapping("/{id}/like/{userid}")
    public void putLike(@PathVariable Long id, @PathVariable Long userid) {
        log.info("Получен HTTP-запрос на лайк фильма: {} от пользователя: {}", id, userid);
        filmService.putLike(id, userid);
        log.info("Успешно обработан HTTP-запрос на лайк фильма: {} от пользователя: {}", id, userid);
    }

    @DeleteMapping("/{id}/like/{userid}")
    public void removeLike(@PathVariable Long id, @PathVariable Long userid) {
        filmService.removeLike(id, userid);
    }

    @GetMapping("/popular")
    public List<Film> getTopFilms(
            @RequestParam(defaultValue = "10") int count) {
        return filmService.getTopPopularFilms(count);
    }
}
