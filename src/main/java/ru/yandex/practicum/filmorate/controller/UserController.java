package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.InvalidBirthdayException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private Map<Long, User> idToUser = new HashMap<>();
    private Long idCounter = 1L;

    @GetMapping
    public List<User> getAll() {
        log.info("Получен HTTP-запрос на получение всех пользователей");
        return new ArrayList<>(idToUser.values());
    }

    @PostMapping
    public User create(@RequestBody @Valid User user) {
        log.info("Получен HTTP-запрос на создание пользователя: {}", user);
        validateName(user);
        validateDate(user);
        user.setId(idCounter++);
        idToUser.put(user.getId(), user);
        log.info("Успешно обработан HTTP-запрос на создание пользователя: {}", user);
        return user;
    }

    private void validateDate(User user) {
        LocalDate currentDate = LocalDate.now();
        if (user.getBirthday().isAfter(currentDate)) {
            String errorMessage = "Дата позже текущей";
            log.error(errorMessage);
            throw new InvalidBirthdayException(errorMessage);
        }
    }

    private void validateName(User user) {
        String newName = Optional.ofNullable(user.getName())
                .filter(name -> !name.isBlank())
                .orElse(user.getLogin());
        user.setName(newName);
    }

    @PutMapping
    public User update(@RequestBody User user) {
        log.info("Получен HTTP-запрос на обновление пользователя: {}", user);
        Long id = user.getId();
        if (!idToUser.containsKey(id)) {
            String errorMessage = String.format("Пользователь с id %d не найден", id);
            log.error(errorMessage);
            throw new UserNotFoundException(errorMessage);
        }
        idToUser.put(user.getId(), user);
        log.info("Успешно обработан HTTP-запрос на обновление пользователя: {}", user);
        return user;
    }
}
