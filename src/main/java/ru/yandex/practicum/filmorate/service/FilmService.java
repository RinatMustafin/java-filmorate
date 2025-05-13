package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.exception.InvalidReleaseDateException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.GenreRepository;
import ru.yandex.practicum.filmorate.repository.LikeRepository;
import ru.yandex.practicum.filmorate.repository.MpaRepository;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service

public class FilmService {

    @Qualifier("filmRepository")
    private final FilmStorage filmStorage;
    @Qualifier("userRepository")
    private final UserStorage userStorage;
    private final MpaRepository mpaRepository;
    private final GenreRepository genreRepository;
    private final LikeRepository likeRepository;

    public List<Film> getAll() {
        List<Film> films = filmStorage.getAll();
        for (Film film : films) {
            List<Genre> genres = genreRepository.findGenresByFilmId(film.getId());
            film.setGenres(genres);
        }
        return films;
    }

    public Film getById(Long id) {
        Film film = filmStorage.getById(id);
        List<Genre> genres = genreRepository.findGenresByFilmId(id);
        film.setGenres(genres);
        return film;
    }

    public Film create(Film film) {
        validateDate(film);

        int mpaId = film.getMpa().getId();
        Mpa mpa = mpaRepository.getById(mpaId);
        film.setMpa(mpa);

        List<Genre> validatedGenres = new ArrayList<>();
        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                Genre existing = genreRepository.findById(genre.getId())
                        .orElseThrow(() -> new GenreNotFoundException("Жанр с id " + genre.getId() + " не найден"));
                validatedGenres.add(existing);
            }
        }
        film.setGenres(validatedGenres);

        Film createdFilm = filmStorage.create(film);
        genreRepository.saveGenresForFilm(createdFilm.getId(), validatedGenres);
        return createdFilm;
    }

    public Film update(Film film) {
        validateDate(film);
        int mpaId = film.getMpa().getId();
        Mpa mpa = mpaRepository.getById(mpaId);
        film.setMpa(mpa);

        List<Genre> validatedGenres = new ArrayList<>();
        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                Genre existing = genreRepository.findById(genre.getId())
                        .orElseThrow(() -> new GenreNotFoundException("Жанр с id " + genre.getId() + " не найден"));
                validatedGenres.add(existing);
            }
        }
        film.setGenres(validatedGenres);
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
            throw new FilmNotFoundException("Фильм не найден: " + filmId);
        }
        if (user == null) {
            throw new UserNotFoundException("Пользователь не найден: " + userId);
        }
        likeRepository.addLike(filmId, userId);
    }

    public void removeLike(Long filmId, Long userId) {
        Film filmById = filmStorage.getById(filmId);
        User user = userStorage.getById(userId);
        if (filmById == null) {
            throw new FilmNotFoundException("Фильм не найден: " + filmId);
        }
        if (user == null) {
            throw new UserNotFoundException("Пользователь не найден: " + userId);
        }
        likeRepository.removeLike(filmId, userId);
    }

    public List<Film> getTopPopularFilms(int count) {
        List<Long> filmIds = likeRepository.getTopFilmIds(count);

        List<Film> films = new ArrayList<>();
        for (Long id : filmIds) {
            Film film = getById(id);
            films.add(film);
        }

        return films;
    }

    public List<Mpa> getAllMpa() {
        return mpaRepository.getAll();
    }

    public Mpa getMpaById(int id) {
        return mpaRepository.getById(id);
    }

    public List<Genre> getAllGenres() {
        return genreRepository.findAll();
    }

    public Genre getGenreById(int id) {
        return genreRepository.findById(id)
                .orElseThrow(() -> new GenreNotFoundException("Жанр с id " + id + " не найден"));
    }

    private void validateDate(Film film) {
        LocalDate neededDate = LocalDate.of(1895, 12, 28);
        if (film.getReleaseDate().isBefore(neededDate)) {
            String errorMessage = "Дата релиза не валидна";
            log.error(errorMessage);
            throw new InvalidReleaseDateException(errorMessage);
        }
    }
}
