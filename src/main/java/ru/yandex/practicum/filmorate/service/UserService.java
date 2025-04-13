package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;


import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;

    public List<User> getAll() {
        List<User> allUsers = userStorage.getAll();
        return allUsers;
    }

    public User getById(Long id) {
        User user = userStorage.getById(id);
        return user;
    }

    public User create(User user) {
        User createdUser = userStorage.create(user);
        return createdUser;
    }

    public User update(User user) {
        User updateUser = userStorage.update(user);
        return updateUser;
    }

    public void deleteById(Long id) {
        userStorage.deleteById(id);
    }

    public void addFriend(Long userId, Long friendId) {
        User user = userStorage.getById(userId);
        User friend = userStorage.getById(friendId);

        if (userId.equals(friendId)) {
            throw new IllegalArgumentException("Нельзя добавиться к самому себе");
        }

        user.addFriend(friendId);
        friend.addFriend(userId);

        userStorage.update(user);
        userStorage.update(friend);
    }

    public void deleteFriend(Long userId, Long friendId) {
        User user = userStorage.getById(userId);
        User friend = userStorage.getById(friendId);

        user.removeFriend(friendId);
        friend.removeFriend(userId);

        userStorage.update(user);
        userStorage.update(friend);
    }

    public List<User> getFriends(Long userId) {
        User user = userStorage.getById(userId);
        return user.getFriendIds().stream()
                .map(id -> userStorage.getById(id))
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(Long userId, Long otherId) {
        Set<Long> userFriendIds = userStorage.getById(userId).getFriendIds();
        Set<Long> otherFriendIds = userStorage.getById(otherId).getFriendIds();
        return userFriendIds.stream()
                .filter(id -> otherFriendIds.contains(id))
                .map(id -> userStorage.getById(id))
                .collect(Collectors.toList());
    }
}
