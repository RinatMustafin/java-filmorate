package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.InvalidBirthdayException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.FriendshipRepository;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    //private JdbcTemplate jdbcTemplate;
    @Qualifier("userRepository")
    private final UserStorage userStorage;
    private final FriendshipRepository friendshipRepository;

    public List<User> getAll() {
        return userStorage.getAll();
    }

    public User getById(Long id) {
        return userStorage.getById(id);
    }

    public User create(User user) {
        validateDate(user);

        return userStorage.create(user);
    }

    public User update(User user) {
        User updateUser = userStorage.update(user);
        return updateUser;
    }

    public void deleteById(Long id) {
        userStorage.deleteById(id);
    }

    public void addFriend(Long userId, Long friendId) {
        User user = userStorage.getById(userId); // репозиторий
        User friend = userStorage.getById(friendId);

        if (user.getId().equals(friend.getId())) {
            throw new IllegalArgumentException("Нельзя добавиться к самому себе");
        }

        user.addFriend(friendId);

        friendshipRepository.addFriend(userId, friendId);
    }

    public void deleteFriend(Long userId, Long friendId) {
        User user = userStorage.getById(userId);
        User friend = userStorage.getById(friendId);

        user.removeFriend(friendId);
        friend.removeFriend(userId);

        friendshipRepository.deleteFriend(userId, friendId);
//       friendshipRepository.deleteFriend(friendId, userId);
    }

    public List<User> getFriends(Long userId) {

        User user = userStorage.getById(userId);

        return friendshipRepository.getFriends(user.getId());
    }

    public List<User> getCommonFriends(Long userId, Long otherId) {

        User user = userStorage.getById(userId); // репозиторий
        User other = userStorage.getById(otherId);

        if (userId.equals(otherId)) {
            throw new IllegalArgumentException("Один и тот же человек");
        }


        return friendshipRepository.getCommonFriends(user.getId(), other.getId());
    }

    private void validateDate(User user) {
        LocalDate currentDate = LocalDate.now();
        if (user.getBirthday().isAfter(currentDate)) {
            String errorMessage = "Дата позже текущей";
            log.error(errorMessage);
            throw new InvalidBirthdayException(errorMessage);
        }
    }
}
