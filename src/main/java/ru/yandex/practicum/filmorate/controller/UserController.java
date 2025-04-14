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

    @PutMapping("/{userid}/friends/{friendid}")
    public void addFriend(
            @PathVariable Long userid,
            @PathVariable Long friendid) {
        log.info("Получен запрос на добавление в друзья: {} -> {}", userid, friendid);
        userService.addFriend(userid, friendid);
        log.info("Обработан запрос на добавление в друзья: {} -> {}", userid, friendid);
    }

    @DeleteMapping("/{userid}/friends/{friendid}")
    public void deleteFriend(
            @PathVariable Long userid,
            @PathVariable Long friendid) {
        log.info("Получен запрос на удаление друга {} у пользователя {}", friendid, userid);
        userService.deleteFriend(userid, friendid);
        log.info("Обработан запрос на удаление друга {} у пользователя {}", friendid, userid);
    }

    @GetMapping("/{userid}/friends")
    public List<User> getFriends(
            @PathVariable Long userid) {
        log.info("Получен запрос на получение друзей у пользователя {}", userid);
        List<User> friends = userService.getFriends(userid);
        log.info("Обработан запрос на получение друзей у пользователя {}", userid);
        return friends;
    }

    @GetMapping("/{userid}/friends/common/{otherid}")
    public List<User> getCommonFriends(
            @PathVariable Long userid,
            @PathVariable Long otherid) {
        log.info("Получен запрос на получение общих друзей между пользователями {} и {}", userid, otherid);
        List<User> commonFriends = userService.getCommonFriends(userid, otherid);
        log.info("Обработан запрос на получение общих друзей между пользователями {} и {}", userid, otherid);
        return commonFriends;
    }
}
