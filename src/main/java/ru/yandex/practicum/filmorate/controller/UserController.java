package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<User> getAll() {
        log.info("Получен HTTP-запрос на получение всех пользователей");
        List<User> allUsers = userService.getAll();
        return allUsers;
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable Long id) {
        log.info("Получен HTTP-запрос на получение пользователя: {}", id);
        User user = userService.getById(id);
        log.debug("Найденный пользователь: {}", user);
        return user;
    }

    @PostMapping
    public User create(@RequestBody @Valid User user) {
        log.info("Получен HTTP-запрос на создание пользователя: {}", user);
        User createdUser = userService.create(user);
        log.info("Успешно обработан HTTP-запрос на создание пользователя: {}", user);
        return createdUser;
    }

    @PutMapping
    public User update(@RequestBody User user) {
        log.info("Получен HTTP-запрос на обновление пользователя: {}", user);
        log.info("Успешно обработан HTTP-запрос на обновление пользователя: {}", user);
        User updateUser = userService.update(user);
        return updateUser;
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        log.info("Получен HTTP-запрос на удаление пользователя с id: {}", id);
        userService.deleteById(id);
        log.info("Успешно обработан HTTP-запрос на удаление пользователя с id: {}", id);
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public void addFriend(
            @PathVariable Long userId,
            @PathVariable Long friendId) {
        log.info("Получен запрос на добавление в друзья: {} -> {}", userId, friendId);
        userService.addFriend(userId, friendId);
        log.info("Обработан запрос на добавление в друзья: {} -> {}", userId, friendId);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public void deleteFriend(
            @PathVariable Long userId,
            @PathVariable Long friendId) {
        log.info("Получен запрос на удаление друга {} у пользователя {}", friendId, userId);
        userService.deleteFriend(userId, friendId);
        log.info("Обработан запрос на удаление друга {} у пользователя {}", friendId, userId);
    }

    @GetMapping("/{userId}/friends")
    public List<User> getFriends(
            @PathVariable Long userId) {
        log.info("Получен запрос на получение друзей у пользователя {}", userId);
        List<User> friends = userService.getFriends(userId);
        log.info("Обработан запрос на получение друзей у пользователя {}", userId);
        return friends;
    }

    @GetMapping("/{userId}/friends/common/{otherId}")
    public List<User> getCommonFriends(
            @PathVariable Long userId,
            @PathVariable Long otherId) {
        log.info("Получен запрос на получение общих друзей между пользователями {} и {}", userId, otherId);
        List<User> commonFriends = userService.getCommonFriends(userId, otherId);
        log.info("Обработан запрос на получение общих друзей между пользователями {} и {}", userId, otherId);
        return commonFriends;
    }
}
